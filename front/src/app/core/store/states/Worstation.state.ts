import { WorkstationDto } from '../models/Workstation/WorkstationDto.model';

export interface WorkstationState {
    workstation : WorkstationDto;
    listWorkstations  : WorkstationDto[];
}

const  workstation : WorkstationDto ={
    id:"",
    name: "" ,
    enable: true,
    tags: []
   };

const listWorkstations  : WorkstationDto[] =[];

export const wsState: WorkstationState = {
     workstation : workstation,
     listWorkstations : listWorkstations
};