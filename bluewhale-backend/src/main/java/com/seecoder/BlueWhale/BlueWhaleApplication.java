package com.seecoder.BlueWhale;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@SpringBootApplication
@EnableCaching
public class BlueWhaleApplication {

	public static void main(String[] args) {

		SpringApplication.run(BlueWhaleApplication.class, args);
	}

}