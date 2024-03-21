import { AttachementDto } from "./AttachementDto.model"
import { Gender } from "./Gender.enum"
import { Role } from "./Role.enum"

export interface UserDto {
    no:number
    id:string,
    createdAt:Date,
    username:string,
    password:string,
    code:string,
    role:Role,
    enabled:boolean,
    firstname:string,
    lastname:string,
    matricule:string,
    phone:number,
    email:string,
    gender:Gender,
    photo:AttachementDto
}
// export interface Users{
//    Userlist:UserDto[],
//    Errormessage:string
// }