package tn.enova.States;

import tn.enova.Models.Commons.Robot;
import tn.enova.Models.Commons.RobotSetting;
import tn.enova.Models.Commons.Tag;

import java.util.ArrayList;
import java.util.List;

public class GlobalState {
    public static List<Robot> listRobots = new ArrayList<>();
    public static List<Tag> listTags = new ArrayList<>();
    public static List<RobotSetting> listRobotSettings = new ArrayList<>();
}
