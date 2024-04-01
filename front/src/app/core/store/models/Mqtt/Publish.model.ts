import { Subscribe } from "./Subscribe.model";


export interface Publish extends Subscribe { 
    payload : string ;
}