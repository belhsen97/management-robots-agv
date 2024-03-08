import { Injectable } from '@angular/core';
import { WorkstationDto } from '../models/Workstation/WorkstationDto.model';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Service } from './globalservice.service';
import { Tag } from '../models/Workstation/Tag.model';

@Injectable({
  providedIn: 'root'
})
export class WorkstationService extends Service {

  public readonly  listTags : Tag[]=[];
  public listWorkstations  : WorkstationDto[] =[];
  
  public tag : Tag ={
    id:1,
    code: "000747643911405335" ,
    description : "description"
   };

  public workstation : WorkstationDto ={
    id:1,
    name: "workstation 1" ,
    enable: true,
    tags:[this.listTags[0],this.listTags[3]],
    robots: []
   };


   constructor(http: HttpClient, router: Router, activeRoute: ActivatedRoute) { 
    super(http, router, activeRoute);
    this.randomDataTags();
    this.randomDataWorkstations();
  }


  randomDataTags(): void {
    for (var i = 0; i < 100; i++) {
      const count = i + 1;
      this.listTags[i] = {
        id: count,
        code: this.getRandomNumber(100, 999).toString()+this.getRandomNumber(100, 999).toString()+this.getRandomNumber(100, 999).toString()+this.getRandomNumber(100, 999).toString(),
        description : "tag description "+count 
      };
    }
  }
  randomDataWorkstations(): void {
    for (var i = 0; i < 30; i++) {
      const count = i + 1;
      this.listWorkstations[i] = {
        id: count,
        name : "Workstation-"+count,
        enable : true,
        tags : [],
        robots: []
      };
    }
  }


}
