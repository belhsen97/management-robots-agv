import { AttachementDto } from "../models/User/AttachementDto.model";
import { Gender } from "../models/User/Gender.enum";
import { Role } from "../models/User/Role.enum";
import { UserDto } from "../models/User/UserDto.model";




export interface UserState {
    gender: Readonly<{ female: Gender; male: Gender }>;
    listRole: ReadonlyArray<{ label: string; value: Role }>;
    listGender: ReadonlyArray<{ label: string; value: Gender }>;
    ListUsers: UserDto[];
    userDto: UserDto;
    defaultPhotoUser: AttachementDto;
}



const genders: Readonly<{ female: Gender; male: Gender }> = {female: Gender.FEMALE, male: Gender.MALE};



const defaultPhotoUser  : AttachementDto= {id: "1",fileName: "user",downloadURL: "assets/images/user/unkhow-user.jpg",fileType: "png",fileSize: 120};
const defaultUserDto: UserDto = {
  no: 1,id: "1",createdAt: new Date(),username: "username",password: "password",code: "code",
  role: Role.OPERATOR,enabled: true,firstname: "empty",lastname: "",matricule: "matricule",
  phone: 12345678,email: "email@exemple.com",gender: Gender.MALE,
  photo: defaultPhotoUser
};

const ListUsers: UserDto[] = [defaultUserDto];


const  listRole  : readonly { label: string; value: Role }[] = [{ label: 'ADMIN', value: Role.ADMIN }, { label: 'MAINTENANCE', value: Role.MAINTENANCE }, { label: 'OPERATOR', value: Role.OPERATOR }];

const  listGender  : readonly { label: string; value: Gender }[] = [{ label: 'FEMALE', value: Gender.FEMALE }, { label: 'MALE', value: Gender.MALE }];




export const userState: UserState = {
    gender: genders,
    listRole:listRole,
    listGender:listGender,
    ListUsers: ListUsers,
    userDto: defaultUserDto,
    defaultPhotoUser: defaultPhotoUser,
};