import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./auth/login/login.component";
import {RegisterComponent} from "./auth/register/register.component";
import {AuthGuardService} from "./helper/auth-guard.service";
import {IndexComponent} from "./layout/index/index.component";
import {NavigationComponent} from "./layout/navigation/navigation.component";

const navigationRoutes: Routes = [
  {path: '', component: NavigationComponent}
];

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'main', component: IndexComponent, canActivate: [AuthGuardService], children: navigationRoutes},
  {path: '', redirectTo: 'main', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
