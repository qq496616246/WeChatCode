package com.hnu.scw.test;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author scw
 * @create 2018-01-20 15:42
 * @desc 测试spring内置的定时器使用，相比，Quartz的任务调度使用简单
 **/
@Component
public class TaskScheduledTest {
    /**
     * 设置定时器的处理方法和处理间隔时间，时间单位是毫秒
     * 要使用的话，记得在spring配置文件中加入<task:annotation-driven/>属性，进行自动扫描
     */
    @Scheduled(fixedRate = 1000)
    public void printTest(){
        System.out.println(System.currentTimeMillis());
    }
}
