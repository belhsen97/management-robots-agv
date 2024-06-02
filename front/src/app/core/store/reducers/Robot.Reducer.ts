import { createReducer, on } from "@ngrx/store";


import { addRobotsuccess, deleteRobotsuccess, loadRobots, loadRobotfail, loadAllRobotsuccess, refreshPannelRobot, updateRobotsuccess, loadDataRobotChartSuccess, refreshRobotssuccess, refreshRobotsuccess, loadRobotsuccess, loadRobotDataBandSuccess, loadSettingRobotSuccess, updateRobotStatusConnectionSuccess } from "../actions/Robot.Action";
import { RobotDto } from "../models/Robot/RobotDto.model";
import { Connection } from "../models/Robot/Connection.enum";
import { ModeRobot } from "../models/Robot/ModeRobot.enum";
import { OperationStatus } from "../models/Robot/OperationStatus.enum";
import { StatusRobot } from "../models/Robot/StatusRobot.enum";
import { CountRobotsProperties } from "../models/Robot/CountRobotsProperties.model";
import { RobotDataChart } from "../models/Robot/RobotDataChart.model";
import { robotState } from "../states/Robot.state";
import { RobotDataBand } from "../models/Robot/RobotDataBand.model";
import { RobotSettingDto } from "../models/Robot/RobotSettingDto.model";

const _robotReducer = createReducer(robotState,
    on(loadRobots, (state) => {
        return {
            ...state//not modifying every anythings whatever the value is comming , it return 
        };
    }),
    on(loadAllRobotsuccess,(state,action)=>{
        const updatedListRobots = action.listRobots.map(robot => ({
            ...robot,
            createdAt: new Date ( robot.createdAt.toString())
        }));
        return {...state,listRobots: [...updatedListRobots],errorMessage: ""};
    }),
    on(loadRobotsuccess,(state,action)=>{
        const robot_ = action.robotinput;
        robot_.createdAt =  new Date ( robot_.createdAt.toString());
        return { ...state, robot: robot_,errorMessage: ""};
    }),
    on(loadSettingRobotSuccess,(state,action)=>{ 
        return {...state,settingRobot: action.setting   ,errorMessage: ""};
    }),

    on(loadRobotDataBandSuccess,(state,action)=>{
        const robotdataband  : RobotDataBand  = action.robotDataBand;
        return {...state,robotDataBand: robotdataband   ,errorMessage: ""};
    }),

    on(loadDataRobotChartSuccess,(state,action)=>{ 
        const robotDataChart : RobotDataChart  = action.robotDataChart;
        return {...state,robotDataChart: robotDataChart,errorMessage: ""
        };
    }),

    on(refreshPannelRobot,(state)=>{
        let waitingCount = 0;
        let runningCount = 0;
        let inactiveCount = 0;

        let autoCount = 0;
        let manualCount = 0;

        let connectedCount = 0;
        let disconnectedCount = 0;

        let emsCount = 0;
        let pauseCount = 0;
        let normalCount = 0; 

        state.listRobots.forEach(robot => {
            switch (robot.statusRobot) {
                case StatusRobot.WAITING:
                    waitingCount++;
                    break;
                case StatusRobot.RUNNING:
                    runningCount++;
                    break;
                case StatusRobot.INACTIVE:
                    inactiveCount++;
                    break;
            }
            switch (robot.modeRobot) {
                case ModeRobot.AUTO:
                    autoCount++;
                    break;
                case ModeRobot.MANUAL:
                    manualCount++;
                    break;
            }
            switch (robot.connection) {
                case Connection.CONNECTED:
                    connectedCount++;
                    break;
                case Connection.DISCONNECTED:
                    disconnectedCount++;
                    break;
            }
            switch (robot.operationStatus) {
                case OperationStatus.EMS:
                    emsCount++;
                    break;
               case OperationStatus.PAUSE:
                    pauseCount++;
                    break;
               case OperationStatus.NORMAL:
                        normalCount++;
                        break;
            }
        });
        const countRobotsStatus: CountRobotsProperties={
        count: state.listRobots.length,
        connected: connectedCount,
        disconnected : disconnectedCount,
        running: runningCount, waiting: waitingCount,inactive: inactiveCount,
        normal: normalCount,ems: emsCount,pause: pauseCount,
        auto: autoCount , manual:manualCount};
        return {  ...state, count: countRobotsStatus};
    }),


    on(loadRobotfail,(state,action)=>{ 
        return{
            ...state,
            listRobots:[],
            errorMessage: action.errorMessage.message
        }
    }),
    on(addRobotsuccess,(state,action)=>{
        const _robot={...action.robotinput};
        _robot.createdAt =  new Date (  state.robot.createdAt.toString());
        return{ ...state,   listRobots:[...state.listRobots,_robot]
        }
    }),
    on(updateRobotsuccess ,(state,action)=>{
        const _robot={...action.robotinput};
        _robot.createdAt =  new Date (  _robot.createdAt.toString());
        const updatedRobot=state.listRobots.map((r:RobotDto)=>{return _robot.id===r.id?_robot:r;});
        return{  ...state, listRobots:[...updatedRobot] , robot : _robot}
    }),
    on(updateRobotStatusConnectionSuccess ,(state,action)=>{
        let connection = Connection.DISCONNECTED;
        if ( ( (action.client!.disconnected_at == undefined )   ? true : action.client!.connected_at > action.client!.disconnected_at )   ){
            connection =  Connection.CONNECTED;}
      if ( ( (action.client!.connected_at == undefined )   ? true : action.client!.connected_at < action.client!.disconnected_at ) ){
        connection = Connection.DISCONNECTED;}
        const updatedRobots = state.listRobots.map((robot: RobotDto) => {
            if (robot.clientid === action.client.clientid) {
              return {
                ...robot,
                connection :  connection,
              };
            }
            return robot;
          });
        return{  ...state, listRobots:[...updatedRobots] }
    }),
    on(refreshRobotssuccess ,(state,action)=>{
        
        const updatedRobots = state.listRobots.map((robot: RobotDto) => {
            if (robot.name === action.robotinput.name) {
              return {
                ...robot,
                connection :  action.robotinput.connection,
                levelBattery :  action.robotinput.levelBattery, 
                modeRobot :  action.robotinput.modeRobot,
                operationStatus :  action.robotinput.operationStatus,
                speed :  action.robotinput.speed,
                statusRobot :  action.robotinput.statusRobot
              };
            }
            return robot;
          });

        return{  ...state, listRobots:[...updatedRobots] }
    }),
    on(refreshRobotsuccess ,(state,action)=>{
        const _robot={...action.robotinput};
        const index = state.listRobots.findIndex(robot => robot.name === _robot.name);
        if (index !== -1) {
            _robot.notice =  state.listRobots[index].notice; 
            _robot.id = state.listRobots[index].id; 
            _robot.workstation = state.listRobots[index].workstation;
         }
   

        return{  ...state, robot:_robot }
    }),
    on(deleteRobotsuccess,(state,action)=>{ 
        const updatedlistRobots = state.listRobots.filter((r:RobotDto)=>{
           return r.id !==action.id
        });
        return{
           ...state, listRobots : updatedlistRobots
        }
    })
);

export function robotReducer(state: any, action: any) {
    return _robotReducer(state, action);

}