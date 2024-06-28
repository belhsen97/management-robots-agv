package com.enova.web.api.Services.Interfaces;


import com.enova.web.api.Services.TaskSchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

@Service("service-task-scheduling")
public class TaskSchedulingServiceImpl implements TaskSchedulingService {
    @Autowired
    private TaskScheduler taskScheduler;

    Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();
    @Override
    public String addScheduleTask(Runnable tasklet, String cronExpression) {
        UUID uuid = UUID.randomUUID();
        String jobId = String.valueOf(uuid);
        System.out.println("Scheduling task with job id: " + jobId + " and cron expression: " + cronExpression);
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet, new CronTrigger(cronExpression, TimeZone.getTimeZone(TimeZone.getDefault().getID())));
        jobsMap.put(jobId, scheduledTask);
        System.out.println(jobsMap.size());
        return jobId;
    }
    @Override
    public String addScheduleTask(Runnable tasklet, Date date) {
        UUID uuid = UUID.randomUUID();
        String jobId = String.valueOf(uuid);
        System.out.println("Scheduling task with job id: " + jobId + " and cron expression: " + date.toString());
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet,date);
        jobsMap.put(jobId, scheduledTask);
        System.out.println(jobsMap.size());
        return jobId;
    }
    @Override
    public boolean removeScheduledTask(String jobId) {
        ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
        if(scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.remove(jobId);      //jobsMap.put(jobId, null);
            return true;
        }
        return false;
    }
}
