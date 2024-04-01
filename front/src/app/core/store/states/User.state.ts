import { ReponseStatus } from "../models/Global/ReponseStatus.enum";
import { AttachementDto } from "../models/User/AttachementDto.model";
import { Gender } from "../models/User/Gender.enum";
import { Role } from "../models/User/Role.enum";
import { UserDto } from "../models/User/UserDto.model";




export interface UserState {
    FEMALE: Readonly<Gender>;
    MALE: Readonly<Gender>;
    listRole: ReadonlyArray<{ label: string; value: Role }>;
    listGender: ReadonlyArray<{ label: string; value: Gender }>;
    ListUsers: UserDto[];
    userDto: UserDto;
    defaultPhotoUser: AttachementDto;
}



const  FEMALE: readonly Gender = Gender.FEMALE;
const  MALE: readonly Gender = Gender.MALE;

const defaultPhotoUser  : AttachementDto= {id: "1",fileName: "user",downloadURL: "assets/images/user/unkhow-user.jpg",fileType: "png",fileSize: 120};
const userDto: UserDto = {
  no: 1,id: "1",createdAt: new Date(),username: "username",password: "password",code: "code",
  role: Role.OPERATOR,enabled: true,firstname: "empty",lastname: "",matricule: "matricule",
  phone: 12345678,email: "email@exemple.com",gender: Gender.MALE,
  photo: defaultPhotoUser
};

const ListUsers: UserDto[] = [userDto];


const  listRole  : readonly { label: string; value: Role }[] = [{ label: 'ADMIN', value: Role.ADMIN }, { label: 'MAINTENANCE', value: Role.MAINTENANCE }, { label: 'OPERATOR', value: Role.OPERATOR }];

const  listGender  : readonly { label: string; value: Gender }[] = [{ label: 'FEMALE', value: Gender.FEMALE }, { label: 'MALE', value: Gender.MALE }];




export interface userState {
    FEMALE: Readonly<Gender>;
    MALE: Readonly<Gender>;
    listRole: ReadonlyArray<{ label: string; value: Role }>;
    listGender: ReadonlyArray<{ label: string; value: Gender }>;
    ListUsers: UserDto[];
    userDto: UserDto;
    defaultPhotoUser: AttachementDto;
}


export const userState: UserState = {
    FEMALE: FEMALE,
    MALE: MALE,
    listRole:listRole,
    listGender:listGender,
    ListUsers: ListUsers,
    userDto: userDto,
    defaultPhotoUser: defaultPhotoUser,
};