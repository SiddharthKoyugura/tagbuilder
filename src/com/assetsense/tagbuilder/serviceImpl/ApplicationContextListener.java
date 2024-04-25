package com.assetsense.tagbuilder.serviceImpl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ApplicationContextListener implements  ApplicationListener<ContextRefreshedEvent>{

	 public static ApplicationContext applicationContext;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
            applicationContext=event.getApplicationContext();	
	}
}