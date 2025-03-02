package com.seecoder.BlueWhale.util;

import com.seecoder.BlueWhale.repository.OrderRepository;
import com.seecoder.BlueWhale.repository.UserRepository;
import com.seecoder.BlueWhale.service.OrderService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
/**
 * @author
 * 任务一
 */
@Component
public class OrderDeleteTask extends QuartzJobBean {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    UserRepository userRepository;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 获取当前触发器实例
        Trigger trigger = context.getTrigger();
        System.out.printf("%s-%s-", trigger.getKey().getName(), trigger.getKey().getGroup());

        // 从触发器的键中获取任务ID
        String id = trigger.getKey().toString().split("-")[1];
        System.out.println("id:" + id + sdf.format(new Date()));

        // 根据任务ID删除对应用户的缓存和订单记录
        orderService.deleteCache(
                userRepository.findById(
                        orderRepository.findById(Integer.parseInt(id)).get().getUserId()).get());
        orderRepository.deleteById(Integer.parseInt(id));
    }

    public JobDetail testQuartz1(String name) {
        // 创建一个 JobDetail 实例，并设置任务的唯一标识符和持久化存储
        return JobBuilder.newJob(OrderDeleteTask.class)
                .withIdentity(name)     //实体名
                .storeDurably()         //持久化存贮
                .build();
    }

    public Trigger testQuartzTrigger1(String taskParameter, JobDetail jobDetail) {
        String name = "order-" + taskParameter;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 14); // 10分钟后的时间

        //cal.add(Calendar.MINUTE,10);//测试时使用10分种
        Date startTime = cal.getTime();

        // 定义简单的调度规则，每隔5秒执行一次，总共执行一次
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5)
                .withRepeatCount(0);

        // 创建一个触发器实例，关联到指定的任务详情和调度规则，并设置开始执行时间
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(name)
                .withSchedule(scheduleBuilder)
                .startAt(startTime)
                .build();
    }
}

