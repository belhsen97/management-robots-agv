package tn.enova.AppRunners;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tn.enova.Models.Commons.Robot;
import tn.enova.Models.Commons.RobotSetting;
import tn.enova.Models.Commons.Tag;
import tn.enova.Services.RobotService;
import tn.enova.Services.RobotSettingService;
import tn.enova.Services.TagService;
import tn.enova.Services.TrackBotService;
import tn.enova.States.GlobalState;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Order(value = 1)
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BeanStartup implements CommandLineRunner {

    private final TrackBotService trackBotService;
    private final TagService tagService;
    private final RobotService robotService;
    private final RobotSettingService robotSettingService;
 //   private final ApplicationContext applicationContext;
   // private final  ExceptionListenerManager exceptionListenerManager;
    @Override
    public void run(String... args) throws InterruptedException {
         this.initDataFromTrackBot();

//        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
//            ExceptionListenerManager exceptionListenerManager = applicationContext.getBean(ExceptionListenerManager.class);
//            exceptionListenerManager.handleException(exception); // Delegate the exception handling
//        });
  //  exceptionListenerManager.initializeHandlers();
// exceptionListenerManager.handleException(  new RessourceFoundException("Cannot fogtrgggxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"));


    }

    private void initDataFromTrackBot ( ){
        CompletableFuture<Void> fetchRobots =   this.trackBotService.getAllRobots()
                .thenAccept(robots -> {
                    this.robotService.insert ((List<Robot>) robots);
                })
                .exceptionally(ex -> {//System.err.println("Failed to fetch robots : " + ex.getMessage());
                    throw new RuntimeException(ex);
                });
        CompletableFuture<Void> fetchTags =  this.trackBotService.getAllTags()
                .thenAccept(tags -> {
                    this.tagService.insert ((List<Tag>) tags);
                })
                .exceptionally(ex -> { //System.err.println("Failed to fetch Tags : " + ex.getMessage());
                    throw new RuntimeException(ex);
                });
        CompletableFuture<Void> fetchRobotSettings = this.trackBotService.getAllRobotSettings()
                .thenAccept(robotSettings -> {
                    this.robotSettingService.insert ((List<RobotSetting>) robotSettings);
                })
                .exceptionally(ex -> {//System.err.println("Failed to fetch Robot Settings : " + ex.getMessage());
                    throw new RuntimeException(ex);
                });
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(fetchRobots, fetchTags, fetchRobotSettings);
        allTasks.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("All tasks finished successfully.");
            } else {
                System.err.println("An error occurred while fetching data.");
                ex.printStackTrace();
                System.exit(-1);
            }
        });
        allTasks.join();
    }
}