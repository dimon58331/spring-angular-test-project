import { Component } from '@angular/core';
import {PostService} from "../../service/post.service";
import {NotificationService} from "../../service/notification.service";
import {ImageUploadService} from "../../service/image-upload.service";
import {Post} from "../../models/Post";
import {CommentService} from "../../service/comment.service";
import {User} from "../../models/User";
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-user-posts',
  templateUrl: './user-posts.component.html',
  styleUrls: ['./user-posts.component.css']
})
export class UserPostsComponent {
  //@ts-ignore
  posts: Post[];
  //@ts-ignore
  user: User;
  public isPostsLoaded = false;
  public isUserLoaded = false;
  constructor(private postService: PostService,
              private notificationService: NotificationService,
              private commentService: CommentService,
              private imageService: ImageUploadService,
              private userService: UserService) {
    this.postService.getPostsForCurrentPerson()
      .subscribe(postsOfCurrentUser => {
        this.posts = postsOfCurrentUser;
        this.getImagesToPosts(this.posts);
        this.getCommentsToPosts(this.posts);
        this.isPostsLoaded = true;
        console.log(this.posts);
      });
    this.userService.getCurrentUser()
      .subscribe(currentUser => {
        this.user = currentUser;
        this.isUserLoaded = true;
        console.log(this.user);
      });
  }

  getImagesToPosts(posts: Post[]): void {
    posts.forEach(post => {
      // @ts-ignore
      this.imageService.getPostImage(post.id)
        .subscribe(value => {
          post.image = value.imageBytes;
        })
    });
  }

  getCommentsToPosts(posts: Post[]): void {
    posts.forEach(post => {
      // @ts-ignore
      this.commentService.getCommentsToPost(post.id)
        .subscribe(value => {
          post.comments = value;
        });
    });
  }

  likePost(postId: number | undefined, postIndex: number): void {
    const post = this.posts[postIndex];
    console.log(post);

    if (!post.likedUsers?.includes(this.user.username)){
      // @ts-ignore
      this.postService.likePost(postId, this.user.username)
        .subscribe(() => {
          post.likedUsers?.push(this.user.username);
          this.notificationService.showSnackBar("Liked!");
        });
    } else {
      // @ts-ignore
      this.postService.likePost(postId, this.user.username)
        .subscribe(() => {
          const index = post.likedUsers?.indexOf(this.user.username, 0);
          // @ts-ignore
          if (index > -1){
            // @ts-ignore
            post.likedUsers?.splice(index, 1);
          }
        });
    }
  }

  postComment(message: string | null, postId: number | undefined, postIndex: number): void {
    const post = this.posts[postIndex];
    console.log(post);
    // @ts-ignore
    this.commentService.addCommentToPost(postId, message)
      .subscribe(value => {
        console.log(value);
        post.comments?.push(value);
      });
  }

  formatImage(img: any): any {
    if (img == null){
      return null;
    }
    return 'data:image/jpeg;base64,' + img;
  }

}
