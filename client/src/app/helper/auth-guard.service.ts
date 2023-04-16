import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {TokenStorageService} from "../service/token-storage.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate{

  constructor(private router: Router, private tokenService: TokenStorageService) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const currentPerson = this.tokenService.getUser();
    if (currentPerson){
      console.log('Current person is active');
      return true;
    }
    console.log('Current user isn\'t active');
    this.router.navigate(['/login'], {queryParams: {returnUrl: state.url}});
    return false;
  }
}
