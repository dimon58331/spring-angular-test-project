import { Component } from '@angular/core';
import {TokenStorageService} from "../../service/token-storage.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent {
  constructor(private tokenStorageService: TokenStorageService,
              private router: Router) {
    if (tokenStorageService.getUser()){
      this.tokenStorageService.logOut();
      this.router.navigate(['/']);
      window.location.reload();
    }
  }
}
