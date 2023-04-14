import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {TokenStorageService} from "../service/token-storage.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate{

  constructor(private router: Router,
              private tokenService: TokenStorageService) {
    console.error('Into the CanActivate');
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot){
    const currentPerson = this.tokenService.getUser();
    if (currentPerson != null){
      console.log('Current person is active');
      if (route.routeConfig?.path?.includes('/logout')){
        console.log('logout');
        this.tokenService.logOut();
        return false;
      }
      return true;
    }
    console.log('Current user isn\'t active');
    this.router.navigate(['/login'], {queryParams: {returnUrl: state.url}});
    return false;
  }
}
