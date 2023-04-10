package com.example.springangular.service;


import com.example.springangular.entity.ImageModel;
import com.example.springangular.entity.Person;
import com.example.springangular.entity.Post;
import com.example.springangular.exception.PostNotFoundException;
import com.example.springangular.repository.ImageModelRepository;
import com.example.springangular.repository.PersonRepository;
import com.example.springangular.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PersonRepository personRepository;
    private final ImageModelRepository imageModelRepository;
    private final PostRepository postRepository;

    @Autowired
    public PostService(PersonRepository personRepository, ImageModelRepository imageModelRepository, PostRepository postRepository) {
        this.personRepository = personRepository;
        this.imageModelRepository = imageModelRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public Post createPost(Post post, Principal principal){
        Person person = getPersonByPrincipal(principal);
        post.setPerson(person);
        post.setLikes(0);
        return postRepository.save(post);
    }

    public Post findPostByIdAndPrincipal(Long postId, Principal principal){
        Person person = getPersonByPrincipal(principal);
        return postRepository.findPostByIdAndPerson(postId, person)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for this username: "
                        + person.getUsername()));
    }

    public List<Post> findAllPostsForPerson(Principal principal){
        Person person = getPersonByPrincipal(principal);
        return postRepository.findAllByPersonOrderByCreatedDateDesc(person);
    }

    @Transactional
    public Post likePost(Long postId, String username){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with this postId not found!"));
        Optional<String> usernameLiked = post.getLikedUsers().stream().filter(s -> s.equals(username)).findAny();
        if (usernameLiked.isEmpty()){
            post.setLikes(post.getLikes() + 1);
            post.getLikedUsers().remove(username);
        }else {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUsers().add(username);
        }
        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long postId, Principal principal){
        Post post = findPostByIdAndPrincipal(postId, principal);
        Optional<ImageModel> imageModel = imageModelRepository.findByPostId(postId);
        postRepository.delete(post);
        imageModel.ifPresent(imageModelRepository::delete);
    }

    public List<Post> findAllPosts(){
        return  postRepository.findAll();
    }

    private Person getPersonByPrincipal(Principal principal){
        Optional<Person> person = personRepository.findPersonByUsername(principal.getName());
        return person.orElseThrow(() -> new UsernameNotFoundException("User with this username not found"));
    }

}
