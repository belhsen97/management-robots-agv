import { RobotDto } from "../Robot/RobotDto.model";
import { Tag } from "./Tag.model";
export interface WorkstationDto {
    no:number
    id: String;
    name: string;
    enable: boolean;
    tags: Tag[];
    robots: RobotDto[];
}