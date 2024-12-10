package tn.enova.Services;

import java.util.Date;

public interface TaskSchedulingService {
    String addScheduleTask(Runnable tasklet, String cronExpression);
    String addScheduleTask(Runnable tasklet, Date date);
    boolean removeScheduledTask(String jobId);
}