import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./auth/login/login.component";
import {RegisterComponent} from "./auth/register/register.component";
import {LogoutComponent} from "./auth/logout/logout.component";
import {AuthGuardService} from "./helper/auth-guard.service";
import {NavigationComponent} from "./layout/navigation/navigation.component";

const routes: Routes = [
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'logout', component: LogoutComponent, canActivate: [AuthGuardService]},
  {path: 'toolBar', component: NavigationComponent, canActivate: [AuthGuardService]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
