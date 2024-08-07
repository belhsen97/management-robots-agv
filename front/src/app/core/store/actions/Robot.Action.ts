import { createAction, props } from "@ngrx/store";
import { RobotDto } from "../models/Robot/RobotDto.model";
import { RobotDataChart } from "../models/Robot/RobotDataChart.model";
import { Subscribe } from "../models/Mqtt/Subscribe.model";
import { RobotDataBand } from "../models/Robot/RobotDataBand.model";
import { RobotSettingDto } from "../models/Robot/RobotSettingDto.model";
import { StatusClientMQTT } from "../models/Global/StatusClientMQTT.model";
 
export const LOAD_ALL_ROBOTS_SUCCESS='[robot page] load all Robots success';
export const LOAD_ROBOT_SUCCESS='[robot page] load Robot success';
export const LOAD_DATA_ROBOT_SUCCESS='[robot page] load Data Robot chart by name success';
export const LOAD_ROBOT_DATA_BAND_SUCCESS='[robot page] load Data Robot band by name success';
export const  LOAD_LIST_ROBOT_DATA_BAND_SUCCESS='[robot page] load List Data Robot band by name success';
export const LOAD_SETTING_ROBOT_SUCCESS='[robot page] load Setting Robot success';

export const LOAD_ROBOT_FAIL='[robot page] load Robot fail';
export const ADD_ROBOT_SUCCESS='[robot page] add Robot success';
export const UPDATE_ROBOT_SUCCESS='[robot page] update Robot success';
export const UPDATE_ROBOT_CNX_STATUS_SUCCESS='[robot page] update Robot success connection status';

export const DELETE_ROBOT_SUCCESS='[robot page] delete Robot success';

export const LOAD_ROBOTS='[robot page] load robot';
export const  LOAD_SETTING_ROBOT='[robot page] load robot';
export const LOAD_ROBOT_BY_Name='[robot page] load robot by name';
export const LOAD_ROBOT_DATA_BAND_BY_NAME='[robot page] load data robot band by name';
export const LOAD_DATA_ROBOT_CHART_BY_NAME_AND_UNIXDATETIME='[robot page] load data robot chart by name and Unix Date time';
export const LOAD_DATA_ROBOT_BAND ='[robot page] load data robot Band all or by name or by Unix Date time';
export const ADD_ROBOT='[robot page] add robot';
export const UPDATE_ROBOT='[robot page] update robot';
export const UPDATE_SETTING_ROBOT='[robot page] update setting robot';
export const DELETE_ROBOT='[robot page] delete robot';
export const REFRESH_PANNEL_ROBOT='[robot page] refresh pannel robot';


export const loadRobotfail=createAction(LOAD_ROBOT_FAIL,props<{errorMessage:any}>())

export const loadDataRobotChartSuccess=createAction(LOAD_DATA_ROBOT_SUCCESS,props<{robotDataChart:RobotDataChart}>());
export const loadRobotDataBandSuccess=createAction(LOAD_ROBOT_DATA_BAND_SUCCESS,props<{robotDataBand:RobotDataBand }>());
export const loadListRobotDataBandSuccess=createAction(LOAD_LIST_ROBOT_DATA_BAND_SUCCESS,props<{list:RobotDataBand[] }>());


export const loadAllRobotsuccess=createAction(LOAD_ALL_ROBOTS_SUCCESS,props<{listRobots:RobotDto[]}>());
export const loadRobotsuccess=createAction(LOAD_ROBOT_SUCCESS,props<{robotinput:RobotDto}>());
export const addRobotsuccess=createAction(ADD_ROBOT_SUCCESS,props<{robotinput:RobotDto}>());
export const updateRobotsuccess=createAction(UPDATE_ROBOT_SUCCESS,props<{robotinput:RobotDto}>());
export const updateRobotStatusConnectionSuccess = createAction(UPDATE_ROBOT_CNX_STATUS_SUCCESS, props<{ client: StatusClientMQTT }>());
export const deleteRobotsuccess=createAction(DELETE_ROBOT_SUCCESS,props<{id:String}>()); 

export const refreshPannelRobot=createAction(REFRESH_PANNEL_ROBOT); 




export const loadDataRobotData=createAction(LOAD_DATA_ROBOT_BAND,props<{name:String | null,start:number | null,end:number | null}>());



export const loadDataRobotChart=createAction(LOAD_DATA_ROBOT_CHART_BY_NAME_AND_UNIXDATETIME,props<{name:String | null,start:number | null,end:number | null}>());


export const loadRobots=createAction(LOAD_ROBOTS);
export const loadRobotByName=createAction(LOAD_ROBOT_BY_Name,props<{name:string}>());
export const addRobot=createAction(ADD_ROBOT,props<{robotinput:RobotDto}>());
export const updateRobot=createAction(UPDATE_ROBOT,props<{id:String,robotinput:RobotDto}>());
export const deleteRobot=createAction(DELETE_ROBOT,props<{id:String}>());


export const loadSettingRobot = createAction(LOAD_SETTING_ROBOT);
export const loadSettingRobotSuccess=createAction(LOAD_SETTING_ROBOT_SUCCESS,props<{setting:RobotSettingDto}>());
export const updateSettingRobot=createAction(UPDATE_SETTING_ROBOT,props<{settinginput:RobotSettingDto}>());




export const listenerAllRobotsSuccess=createAction('[ROBOT] Listener All Robots Success',props<{robotinput:RobotDto}>());
export const listenerAllRobotsByPropertySuccess=createAction('[ROBOT] Listener All Robots By Property Success',props<{topic:String,value:any}>());
export const listenerRobotsuccess=createAction('[ROBOT] Listener Robot Success',props<{robotinput:RobotDto}>());
export const listenerRobotByPropertySuccess=createAction('[ROBOT] Listener Robot By Property Success',props<{topic:String,value:any}>());

export const startListenerAllRobots=createAction('[ROBOT] Start Listener All Robots',props<{ subscribe: Subscribe }>());
export const startListenerAllRobotsByProperty=createAction('[ROBOT] Start Listener All Robots By Property',props<{ subscribe: Subscribe }>());
export const startListenerRobot=createAction('[ROBOT] Start Listener Robot',props<{ subscribe: Subscribe }>());
export const startListenerRobotByProperty=createAction('[ROBOT] Start Listener Robot By Property',props<{ subscribe: Subscribe }>());

export const stopListenerAllRobots=createAction('[ROBOT] Stop Listener All Robots');
export const stopListenerAllRobotsByProperty=createAction('[ROBOT] Stop Listener All Robots By Property');
export const stopListenerRobot=createAction('[ROBOT] Stop Listener Robot');
export const stopListenerRobotByProperty=createAction('[ROBOT] Stop Listener Robot By Property');