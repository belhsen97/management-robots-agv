import { createFeatureSelector, createSelector } from "@ngrx/store";
import { RobotState } from "../states/Robot.state";

const getRobotState=createFeatureSelector<RobotState>('robotstate'); //accessing the current state and create feature selector

export const getListRobot=createSelector(getRobotState,(state)=>{  //create the individual selectors for that be how the class is create 
    return state.listRobots;
});
export const getRobot=createSelector(getRobotState,(state)=>{return state.robot;});
export const getConnectionFromRobot=createSelector(getRobotState,(state)=>{return state.robot.connection;});
export const getModeFromRobot=createSelector(getRobotState,(state)=>{return state.robot.modeRobot;});
export const getStatusRobotFromRobot=createSelector(getRobotState,(state)=>{return state.robot.statusRobot;});
export const getOperationStatusFromRobot=createSelector(getRobotState,(state)=>{return state.robot.operationStatus;});
export const getLevelBatteryFromRobot=createSelector(getRobotState,(state)=>{return state.robot.levelBattery;});
export const getSpeedFromRobot=createSelector(getRobotState,(state)=>{return state.robot.speed;});

export const getCountRobotsProperties=createSelector(getRobotState,(state)=>{  return state.count;});
export const getDataRobotChart =createSelector(getRobotState,(state)=>{  return state.robotDataChart;  });
export const getRobotDataBand =createSelector(getRobotState,(state)=>{ return state.robotDataBand;   });
export const getListRobotDataBand =createSelector(getRobotState,(state)=>{ return state.listRobotDataBand;   });
export const getSettingRobot=createSelector(getRobotState,(state)=>{  return state.settingRobot;  });