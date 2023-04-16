import {Component, OnInit} from '@angular/core';
import {Post} from "../../models/Post";
import {User} from "../../models/User";
import {PostService} from "../../service/post.service";
import {UserService} from "../../service/user.service";
import {CommentService} from "../../service/comment.service";
import {NotificationService} from "../../service/notification.service";
import {ImageUploadService} from "../../service/image-upload.service";

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit{
  isPostsLoaded = false;
  isUserLoaded = false;
  posts: Post[];
  user: User;
  constructor(private postService: PostService,
              private userService: UserService,
              private commentService: CommentService,
              private notificationService: NotificationService,
              private imageService: ImageUploadService) {
    // @ts-ignore
    this.user = null;
    // @ts-ignore
    this.posts = null;
    console.log("check constructor index.component");
  }

  ngOnInit(): void {
    console.log("check ngOnInit index.component");
    this.postService.getAllPosts()
      .subscribe(value => {
        console.log(value);
        this.posts = value;
        this.getImagesToPosts(this.posts);
        this.getCommentsToPosts(this.posts);
        this.isPostsLoaded = true;
      }, error => {
          console.error(error);
        }
      );
    this.userService.getCurrentUser()
      .subscribe(value => {
        console.log(value);
        this.user = value;
        this.isUserLoaded = true;
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
          })
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
