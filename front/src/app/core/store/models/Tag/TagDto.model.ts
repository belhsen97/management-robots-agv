import { WorkstationDto } from "../Workstation/WorkstationDto.model";

export interface TagDto {
    id: string;
    code: string;
    description: string;
    workstation:WorkstationDto;
}