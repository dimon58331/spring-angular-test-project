import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {NotificationService} from "../../service/notification.service";
import {UserService} from "../../service/user.service";
import {User} from "../../models/User";

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent {
  // @ts-ignore
  public profileEditForm: FormGroup;
  constructor(private dialogMat: MatDialogRef<EditUserComponent>,
              private fb: FormBuilder,
              private notificationService: NotificationService,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private userService: UserService) {
    this.profileEditForm = this.createProfileForm();
  }

  createProfileForm(): FormGroup {
    return this.fb.group({
      name: [
        this.data.user.name,
        Validators.compose([Validators.required])
      ],
      surname: [
        this.data.user.surname,
        Validators.compose([Validators.required])
      ],
      bio: [
        this.data.user.bio,
        Validators.compose([Validators.required])
      ]
    });
  }

  submit(): void {
    this.userService.updateUser(this.updateUser())
      .subscribe((data)=>{
        console.log(data);
        this.notificationService.showSnackBar("User updated successfully!");
        this.dialogMat.close();
      });
  }

  private updateUser(): User {
    this.data.user.name = this.profileEditForm.value.name;
    this.data.user.surname = this.profileEditForm.value.surname;
    this.data.user.bio = this.profileEditForm.value.bio;
    console.log(this.data.user);
    return this.data.user;
  }

  closeDialog(): void {
    this.dialogMat.close();
  }
}
