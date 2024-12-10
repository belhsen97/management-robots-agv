import { WorkstationDto } from "../Workstation/WorkstationDto.model";

export interface TagDto {
    id: String;
    code: String;
    workstation:WorkstationDto;
}