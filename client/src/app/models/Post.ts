import {Comment} from "./Comment";
import {User} from "./User";

export interface Post{
  id?:number;
  title:string;
  description:string;
  location:string;
  image?:File;
  likes?:number;
  likedUsers?:string[];
  comments?: Comment[];
  person: User;
}
