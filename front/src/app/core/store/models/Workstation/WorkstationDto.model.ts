import { RobotDto } from "../Robot/RobotDto.model";
import { TagDto } from "../Tag/TagDto.model";
export interface WorkstationDto {
    id: String;
    name: string;
    enable: boolean;
    description: string;
    tags?: TagDto[];
}