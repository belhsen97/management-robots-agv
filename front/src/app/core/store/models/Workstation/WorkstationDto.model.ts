import { RobotDto } from "../Robot/RobotDto.model";
import { Tag } from "./Tag.model";
export interface WorkstationDto {
    id: number;
    name: string;
    enable: boolean;
    tags: Tag[];
    robots: RobotDto[];
}