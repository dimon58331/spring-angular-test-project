import {Comment} from "./Comment";

export interface Post{
  id?:number;
  title:string;
  description:string;
  location:string;
  image?:File;
  likes?:number;
  likedUsers?:string[];
  comments?: Comment[];
  username?:string;
}
