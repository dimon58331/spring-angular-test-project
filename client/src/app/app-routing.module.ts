import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./auth/login/login.component";
import {RegisterComponent} from "./auth/register/register.component";
import {AuthGuardService} from "./helper/auth-guard.service";
import {IndexComponent} from "./layout/index/index.component";
import {ProfileComponent} from "./user/profile/profile.component";
import {UserPostsComponent} from "./user/user-posts/user-posts.component";
import {AddPostComponent} from "./user/add-post/add-post.component";

const profileRoutes: Routes = [
  {path: '', component: UserPostsComponent, canActivate: [AuthGuardService]}
];

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'main', component: IndexComponent, canActivate: [AuthGuardService]},
  {path: 'profile', component: ProfileComponent, canActivate: [AuthGuardService], children: profileRoutes},
  {path: '**', redirectTo: 'main', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
