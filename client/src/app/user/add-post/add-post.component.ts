import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {NotificationService} from "../../service/notification.service";
import {PostService} from "../../service/post.service";
import {Post} from "../../models/Post";
import {ImageUploadService} from "../../service/image-upload.service";

@Component({
  selector: 'app-add-post',
  templateUrl: './add-post.component.html',
  styleUrls: ['./add-post.component.css']
})
export class AddPostComponent {
  //@ts-ignore
  public postForm: FormGroup;
  // @ts-ignore
  public selectedFile: File;
  public previewImgURL: any;
  public fileIsLoaded = false;

  constructor(private dialogMat: MatDialogRef<AddPostComponent>,
              private fb: FormBuilder,
              private notificationService: NotificationService,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private postService: PostService,
              private imageService: ImageUploadService) {
    this.postForm = this.createPostForm();
  }

  createPostForm(): FormGroup {
    return this.fb.group({
      title: [
        '', Validators.compose([Validators.required])
      ],
      description: [
        '', Validators.compose([Validators.required])
      ],
      location: [
        '', Validators.compose([Validators.required])
      ]
    });
  }

  submit(): void{
    this.postService.createPost(this.createPost())
      .subscribe(data=>{
        console.log(data);
        this.imageService.uploadImageToPost(this.selectedFile, data.id)
          .subscribe(imageDate => {
            console.log(imageDate);
          });
        this.dialogMat.close();

        sessionStorage.setItem('reloadAfterPageLoad', 'true');
        sessionStorage.setItem('notification-message', 'Post created successfully!');
        window.location.reload();

      });
  }

  private createPost(): Post {
    this.data.post.title = this.postForm.value.title;
    this.data.post.description = this.postForm.value.description;
    this.data.post.location = this.postForm.value.location;
    console.log(this.data.post);
    return this.data.post;
  }

  closeDialog(): void {
    this.dialogMat.close();
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
    const reader = new FileReader();
    reader.readAsDataURL(this.selectedFile);
    reader.onload = () => {
      this.previewImgURL = reader.result;
    };
  }

  onUpload(): void {
    if (this.selectedFile != null){
      this.data.post.image = this.selectedFile;
      this.fileIsLoaded = true;
      if (this.data.post.image){
        this.notificationService.showSnackBar('Post image loaded successfully!');
      }
    }
  }
}
