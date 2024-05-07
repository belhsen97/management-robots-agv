export type Plot = { text: string, from: number, to: number };
export interface RobotDataBand {
    name: string;
    connection: {
        average: { connected: number, desconnected: number },
        interval: { connected: Plot[], desconnected: Plot[] }
    };
    mode: {
        average: { auto: number, manual: number },
        interval: { auto: Plot[], manual: Plot[] }
    };
    operationStatus: {
        average: { normal: number, ems: number , pause: number},
        interval: { normal: Plot[], ems: Plot[] , pause: Plot[]}
    };
    statusRobot: {
        average: { running: number, inactive: number , waiting: number},
        interval: { running: Plot[], inactive: Plot[] , waiting: Plot[]}
    };
    battery: {
        average: { charge: number, standby: number , discharge: number},
        interval: { charge: Plot[], standby: Plot[] , discharge: Plot[]}
    };
    speed: {
        average: { max: number, standby: number , min: number},
        interval: { max: Plot[], standby: Plot[] , min: Plot[]}
    };
}