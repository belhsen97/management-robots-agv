import { TagDto } from '../models/Tag/TagDto.model';
import { wsState } from './Worstation.state';

export interface TagState {
    tag : TagDto;
    listTags  : TagDto[];
}

const  listTags : TagDto[]=[];
const  tag : TagDto ={id:'',code:'',description:'',workstation:wsState.workstation};

export const tagState: TagState = {
    tag : tag,
    listTags  : listTags
};