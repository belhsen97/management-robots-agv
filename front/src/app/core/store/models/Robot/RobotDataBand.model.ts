export type Plot = { text: string, from: number, to: number };
export interface RobotDataBand {
    name: string;
    connection: {
        average: { connected: number, desconnected: number },
        duration: { connected: number, desconnected: number },
        frequency: { connected: number, desconnected: number },
        interval: { connected: Plot[], desconnected: Plot[] }
    };
    mode: {
        average: { auto: number, manual: number },
        duration: { auto: number, manual: number },
        frequency: { auto: number, manual: number },
        interval: { auto: Plot[], manual: Plot[] }
    };
    operationStatus: {
        average: { normal: number, ems: number , pause: number},
        duration: { normal: number, ems: number , pause: number},
        frequency: { normal: number, ems: number , pause: number},
        interval: { normal: Plot[], ems: Plot[] , pause: Plot[]}
    };
    statusRobot: {
        average: { running: number, inactive: number , waiting: number},
        duration:{ running: number, inactive: number , waiting: number},
        frequency: { running: number, inactive: number , waiting: number},
        interval: { running: Plot[], inactive: Plot[] , waiting: Plot[]}
    };
    battery: {
        average: { charge: number, standby: number , discharge: number},
        duration:{ charge: number, standby: number , discharge: number},
        frequency:{ charge: number, standby: number , discharge: number},
        interval: { charge: Plot[], standby: Plot[] , discharge: Plot[]}
    };
    speed: {
        average: { max: number, standby: number , min: number},
        duration: { max: number, standby: number , min: number},
        frequency: { max: number, standby: number , min: number},
        interval: { max: Plot[], standby: Plot[] , min: Plot[]}
    };
}