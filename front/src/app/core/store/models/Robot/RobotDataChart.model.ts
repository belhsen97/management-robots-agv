type DataPoint = [number, number];
export interface RobotDataChart {
    name: string;
    speed: DataPoint[];
    battery: DataPoint[];
    statusRobot: DataPoint[];
    operationStatus: DataPoint[];
    connectionPlotBand:[{from: number, to: number ,text : String }]
    connectionPlotLine:[{value: number, text : String }]
    modePlotBand:[{from: number, to: number ,text : String }]
    modePlotLine:[{value: number, text : String }]
}