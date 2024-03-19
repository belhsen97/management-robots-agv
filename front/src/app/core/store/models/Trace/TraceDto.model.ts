import { UserDto } from "../User/UserDto.model";

export interface TraceDto {
    id:string,
    username:string,
    timestamp:Date,
    description:string,
    className:string,
    methodName:string,
    user:UserDto
}