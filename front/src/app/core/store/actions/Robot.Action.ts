import { createAction, props } from "@ngrx/store";
import { RobotDto } from "../models/Robot/RobotDto.model";
import { RobotDataChart } from "../models/Robot/RobotDataChart.model";
import { Subscribe } from "../models/Mqtt/Subscribe.model";
import { RobotProperty } from "../models/Robot/RobotProperty.model";
 
export const LOAD_ALL_ROBOTS_SUCCESS='[robot page] load all Robots success';
export const LOAD_ROBOT_SUCCESS='[robot page] load Robot success';
export const LOAD_DATA_ROBOT_SUCCESS='[robot page] load Data Robot chart by name success';
export const LOAD_ROBOT_FAIL='[robot page] load Robot fail';
export const ADD_ROBOT_SUCCESS='[robot page] add Robot success';
export const UPDATE_ROBOT_SUCCESS='[robot page] update Robot success';
export const REFRESH_ROBOTS_SUCCESS='[robot page] refresh Robots success';
export const REFRESH_ROBOT_SUCCESS='[robot page] refresh Robot success';
export const DELETE_ROBOT_SUCCESS='[robot page] delete Robot success';

export const LOAD_ROBOTS='[robot page] load robot';
export const LOAD_ROBOT_BY_Name='[robot page] load robot by name';
export const LOAD_DATA_ROBOT_PEOPERTY_BY_NAME='[robot page] load data robot property by name';
export const LOAD_DATA_ROBOT_CHART_BY_NAME='[robot page] load data robot chart by name';
export const LOAD_DATA_ROBOT_CHART_BY_NAME_AND_UNIXDATETIME='[robot page] load data robot chart by name and Unix Date time';
export const ADD_ROBOT='[robot page] add robot';
export const UPDATE_ROBOT='[robot page] update robot';
export const DELETE_ROBOT='[robot page] delete robot';
export const REFRESH_PANNEL_ROBOT='[robot page] refresh pannel robot';
export const REFRESH_ROBOTS='[robot page] refresh Robots';
export const REFRESH_ROBOT='[robot page] refresh Robot';
export const STOP_REFRESH_ROBOTS='[robot page] stop refresh Robots';
export const STOP_REFRESH_ROBOT='[robot page] stop refresh Robot';

export const loadRobotfail=createAction(LOAD_ROBOT_FAIL,props<{errorMessage:any}>())

export const loadDataRobotChartSuccess=createAction(LOAD_DATA_ROBOT_SUCCESS,props<{robotDataChart:RobotDataChart}>());
export const loadDataRobotPropertySuccess=createAction(LOAD_DATA_ROBOT_SUCCESS,props<{listRobotPropertys:RobotProperty[] }>());

export const loadAllRobotsuccess=createAction(LOAD_ALL_ROBOTS_SUCCESS,props<{listRobots:RobotDto[]}>());
export const loadRobotsuccess=createAction(LOAD_ROBOT_SUCCESS,props<{robotinput:RobotDto}>());
export const addRobotsuccess=createAction(ADD_ROBOT_SUCCESS,props<{robotinput:RobotDto}>());
export const updateRobotsuccess=createAction(UPDATE_ROBOT_SUCCESS,props<{robotinput:RobotDto}>());
export const deleteRobotsuccess=createAction(DELETE_ROBOT_SUCCESS,props<{id:String}>()); 

export const refreshPannelRobot=createAction(REFRESH_PANNEL_ROBOT); 
export const refreshRobotssuccess=createAction(REFRESH_ROBOTS_SUCCESS,props<{robotinput:RobotDto}>());
export const refreshRobotsuccess=createAction(REFRESH_ROBOT_SUCCESS,props<{robotinput:RobotDto}>());




export const loadDataRobotPropertybyName=createAction(LOAD_DATA_ROBOT_PEOPERTY_BY_NAME,props<{name:String}>());
export const loadDataRobotChartbyName=createAction(LOAD_DATA_ROBOT_CHART_BY_NAME,props<{name:String}>());
export const loadDataRobotChartByNameAndUnixDatetime=createAction(LOAD_DATA_ROBOT_CHART_BY_NAME_AND_UNIXDATETIME,props<{name:String,start:number,end:number}>());

export const refreshRobots=createAction(REFRESH_ROBOTS,props<{ subscribe: Subscribe }>()); 
export const refreshRobot=createAction(REFRESH_ROBOT,props<{ subscribe: Subscribe }>()); 
export const stopRefreshRobots=createAction(STOP_REFRESH_ROBOTS);
export const stopRefreshRobot=createAction(STOP_REFRESH_ROBOT); 

export const loadRobots=createAction(LOAD_ROBOTS);
export const loadRobotByName=createAction(LOAD_ROBOT_BY_Name,props<{name:string}>());
export const addRobot=createAction(ADD_ROBOT,props<{robotinput:RobotDto}>());
export const updateRobot=createAction(UPDATE_ROBOT,props<{id:String,robotinput:RobotDto}>());
export const deleteRobot=createAction(DELETE_ROBOT,props<{id:String}>());