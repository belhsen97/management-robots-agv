import { TypeProperty } from "./TypeProperty.enum";

export interface RobotProperty {
    name: String;
    timestamp: Date;
    type: TypeProperty;
    value: String;
}