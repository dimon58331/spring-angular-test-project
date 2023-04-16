import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../service/auth.service";
import {TokenStorageService} from "../../service/token-storage.service";
import {Router} from "@angular/router";
import {NotificationService} from "../../service/notification.service";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent{
  public loginForm: FormGroup;

  constructor(
    private authService: AuthService,
    private tokenStorage: TokenStorageService,
    private notificationService: NotificationService,
    private router: Router,
    private fb: FormBuilder) {
    this.loginForm = this.fb.group({
      username: ['', Validators.compose([Validators.required])],
      password: ['', Validators.compose([Validators.required])]
    });

    if (this.tokenStorage.getUser()){
      this.router.navigate(['/main']);
    }
  }

  submit(): void {
    this.authService.login({
      username: this.loginForm.value.username,
      password: this.loginForm.value.password
    }).subscribe(value => {
      console.log(value)

      this.tokenStorage.saveToken(value.token);
      this.tokenStorage.saveUser(value);

      this.router.navigate(['/main']);
      this.notificationService.showSnackBar('Successfully logged in');
    }, error => {
      console.log(error);
      sessionStorage.setItem('reloadAfterPageLoad', 'true');
      sessionStorage.setItem('notification-message', 'Invalid username or password!');
      window.location.reload();
    })
  }
}
