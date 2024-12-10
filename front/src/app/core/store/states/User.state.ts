import { AttachementDto } from "../models/User/AttachementDto.model";
import { Gender } from "../models/User/Gender.enum";
import { Role } from "../models/User/Role.enum";
import { UserDto } from "../models/User/UserDto.model";


export interface UserState {
    gender: Readonly<{ female: Gender; male: Gender }>;
    listRole: ReadonlyArray<{ label: string; value: Role }>;
    listGender: ReadonlyArray<{ label: string; value: Gender }>;
    listPathPermision : ReadonlyArray<{ role: Role; path: String }>;
    ListUsers: UserDto[];
    userDto: UserDto | null;
    defaultPhotoUser: AttachementDto;
}

const listPathPermision : readonly { role: Role; path: String  }[] = [
  { role: Role.ADMIN, path: '/user/tracing' },
  { role: Role.ADMIN, path: '/user/list' },
  { role: Role.ADMIN, path: '/user/edit' },
  { role: Role.MAINTENANCE, path: '/user/edit' },
  { role: Role.OPERATOR, path: '/user/edit' },

  { role: Role.MAINTENANCE, path: '/robot-avg/**' },
  { role: Role.ADMIN, path: '/robot-avg/**' },

  { role: Role.MAINTENANCE, path: '/workstation/**' },
  { role: Role.ADMIN, path: '/workstation/**' },

  { role: Role.ADMIN, path: '/notifications' },
  { role: Role.MAINTENANCE, path: '/notifications' },
  { role: Role.OPERATOR, path: '/notifications' },

  { role: Role.MAINTENANCE, path: '/statistic/**' },
  { role: Role.ADMIN, path: '/statistic/**' },

  { role: Role.OPERATOR, path: '/dashboard/table' },
  { role: Role.MAINTENANCE, path: '/dashboard/**' },
  { role: Role.ADMIN, path: '/dashboard/**' },

  { role: Role.MAINTENANCE, path: '/global/setting' },
  { role: Role.ADMIN, path: '/global/setting' },

  { role: Role.ADMIN, path: '/mail/**' },
  { role: Role.MAINTENANCE, path: '/mail/**' },
  { role: Role.OPERATOR, path: '/mail/**' },
];

const genders: Readonly<{ female: Gender; male: Gender }> = {female: Gender.FEMALE, male: Gender.MALE};
const defaultPhotoUser  : AttachementDto= {id: "1",fileName: "user",downloadURL: "assets/images/user/unkhow-user.jpg",fileType: "png",fileSize: 120};
export const defaultUserDto: UserDto = {
  no: 1,id: "1",createdAt: new Date().getTime(),username: "username",password: "password",code: "code",
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
    userDto: null,
    defaultPhotoUser: defaultPhotoUser,
    listPathPermision : listPathPermision
};