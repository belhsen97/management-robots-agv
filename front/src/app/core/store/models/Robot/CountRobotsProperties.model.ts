export interface CountRobotsProperties {
    count: number;

    connected: number;
    disconnected:number;

    running: number;
    waiting:number;
    inactive:number;

    normal: number;
    ems: number;
    pause: number;

    auto: number;
    manual:number;
}