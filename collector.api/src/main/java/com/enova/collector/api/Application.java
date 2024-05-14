package com.enova.collector.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class Application {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);


		//Thread.setDefaultUncaughtExceptionHandler(new Random());
		//throw new Exception("Exception");
	}


}
/*class Random implements Thread.UncaughtExceptionHandler {
	public void uncaughtException(Thread t, Throwable e)
	{System.out.println("Exception Handled " + e);}
}*/