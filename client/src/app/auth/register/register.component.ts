import { Component } from '@angular/core';
import {AuthService} from "../../service/auth.service";
import {NotificationService} from "../../service/notification.service";
import {Router} from "@angular/router";
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {MyErrorStateMatcher} from "./validator/password-matcher";
import {TokenStorageService} from "../../service/token-storage.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  public registerForm: FormGroup;
  public matcher = new MyErrorStateMatcher();
  constructor(
    private authService: AuthService,
    private tokenStorage: TokenStorageService,
    private notificationService: NotificationService,
    private router: Router,
    private formBuilder: FormBuilder
  ) {
    if (this.tokenStorage.getUser()){
      this.router.navigate(['main']);
    }

    this.registerForm = this.formBuilder.group({
      username: ['', Validators.compose([Validators.required])],
      email: ['', Validators.compose([Validators.required, Validators.email])],
      name: ['', Validators.compose([Validators.required])],
      surname: ['', Validators.compose([Validators.required])],
      password: ['', Validators.compose([Validators.required])],
      confirmPassword: ['']
    }, {validators: this.checkPassword});
  }

  checkPassword: ValidatorFn = (group: AbstractControl): ValidationErrors | null => {
    let pass = group.get('password')?.value;
    let confirmPass = group.get('confirmPassword')?.value;
    return pass === confirmPass ? null : { notSame: true};
  }

  submit(): void {
    this.authService.register({
      username: this.registerForm.value.username,
      email: this.registerForm.value.email,
      name: this.registerForm.value.name,
      surname: this.registerForm.value.surname,
      password: this.registerForm.value.password,
      confirmPassword: this.registerForm.value.confirmPassword
    }).subscribe(value => {
      console.log(value);

      this.notificationService.showSnackBar(value.message);
      this.router.navigate(['/login']);
    }, error => {
      console.log(error);
      sessionStorage.setItem('reloadAfterPageLoad', 'true');
      sessionStorage.setItem('notification-message', 'Incorrect data!');
      window.location.reload();
    });
  }

}
