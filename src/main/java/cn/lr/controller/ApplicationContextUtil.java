package cn.lr.controller;

import java.util.Calendar;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.lr.service.employee.TimedTaskService;
import cn.lr.util.LoggerUtil;

@Component
@EnableScheduling
public class ApplicationContextUtil implements ApplicationContextAware {
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return context;
	}

	public static Object getBean(String name) {
	        return getApplicationContext().getBean(name);
	}
	
	@Scheduled(cron = "0 59 23 * * ?")
	public void autoDay() {
		LoggerUtil.LOGGER.info("-------------enter 每日定时任务--------------------");
		TimedTaskService timedTaskService = (TimedTaskService)getBean("TimedTaskService");
		timedTaskService.timedDay();
		LoggerUtil.LOGGER.info("-------------end 每日定时任务--------------------");
	}
	@Scheduled(cron = "0 10 0 ? * sun")
	public void autoWeek() {
		LoggerUtil.LOGGER.info("-------------enter 每周定时任务--------------------");
		TimedTaskService timedTaskService = (TimedTaskService)getBean("TimedTaskService");
		timedTaskService.timedWeek();
		LoggerUtil.LOGGER.info("-------------end 每周定时任务--------------------");
	}
	@Scheduled(cron = "0 20 0 28-31 * ?")
	public void autoMon() {
		final Calendar c = Calendar.getInstance();
		if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
			LoggerUtil.LOGGER.info("-------------enter 每月定时任务--------------------");
			TimedTaskService timedTaskService = (TimedTaskService)getBean("TimedTaskService");
			timedTaskService.timedMon();
			LoggerUtil.LOGGER.info("-------------end 每月定时任务--------------------");
		}
	}
	
}

