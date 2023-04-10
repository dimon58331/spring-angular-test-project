package com.example.springangular.service;

import com.example.springangular.entity.ImageModel;
import com.example.springangular.entity.Person;
import com.example.springangular.entity.Post;
import com.example.springangular.exception.ImageModelNotFoundException;
import com.example.springangular.exception.PostNotFoundException;
import com.example.springangular.repository.ImageModelRepository;
import com.example.springangular.repository.PersonRepository;
import com.example.springangular.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@Transactional(readOnly = true)
public class ImageUploadService {
    private final static Logger LOG = LoggerFactory.getLogger(ImageUploadService.class);
    private final ImageModelRepository imageModelRepository;
    private final PersonRepository personRepository;
    private final PostRepository postRepository;

    @Autowired
    public ImageUploadService(ImageModelRepository imageModelRepository, PersonRepository personRepository, PostRepository postRepository) {
        this.imageModelRepository = imageModelRepository;
        this.personRepository = personRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public ImageModel uploadImageToPerson(MultipartFile file, Principal principal) throws IOException {
        Person person = getPersonByPrincipal(principal);
        Optional<ImageModel> userProfileImage = imageModelRepository.findByPersonId(person.getId());
        userProfileImage.ifPresent(imageModelRepository::delete);

        ImageModel newUserProfileImage = new ImageModel();
        newUserProfileImage.setPersonId(person.getId());
        newUserProfileImage.setImageBytes(compressBytes(file.getBytes()));
        newUserProfileImage.setName(file.getOriginalFilename());

        return imageModelRepository.save(newUserProfileImage);
    }

    @Transactional
    public ImageModel uploadImageToPost(MultipartFile file, Long postId) throws IOException{
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        Optional<ImageModel> postImage = imageModelRepository.findByPostId(postId);
        postImage.ifPresent(imageModelRepository::delete);

        ImageModel newPostImage = new ImageModel();
        newPostImage.setPostId(post.getId());
        newPostImage.setImageBytes(compressBytes(file.getBytes()));
        newPostImage.setName(file.getOriginalFilename());

        return imageModelRepository.save(newPostImage);
    }

    public ImageModel getPersonImage(Principal principal){
        Person person = getPersonByPrincipal(principal);
        ImageModel imageModel = imageModelRepository.findByPersonId(person.getId())
                .orElseThrow(() -> new ImageModelNotFoundException("Image cannot be found"));

        imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        return imageModel;
    }

    public ImageModel getPostImage(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        ImageModel imageModel = imageModelRepository.findByPostId(post.getId())
                .orElseThrow(() -> new ImageModelNotFoundException("Image cannot be found"));
        imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));

        return imageModel;
    }

    private Person getPersonByPrincipal(Principal principal){
        String username = principal.getName();
        return personRepository.findPersonByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username '" + username + "' not found"));
    }

    private byte[] compressBytes(byte[] data){
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()){
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        }catch (IOException e){
            LOG.error("Cannot compress Bytes");
        }
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data){
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while(!inflater.finished()){
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        }catch (IOException | DataFormatException e){
            LOG.error("Cannot decompress Bytes");
        }
        return outputStream.toByteArray();
    }
}
