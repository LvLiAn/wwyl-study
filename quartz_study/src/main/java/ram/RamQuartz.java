package ram;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * @Auther: lvla
 * @Date: 2018/12/22 10:39
 * @Description:
 *   RAM 和 JDBC方式两种方式区别，jdbc方式项目中配置有quartz.properties文件
 *
 */
public class RamQuartz {

    /**
     * 创建quartz调度器
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // 1、创建Scheduler工厂
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        // 2、获取调度器 Scheduler 首次调度，初始化加载quartz.properties配置文件
        Scheduler scheduler = schedulerFactory.getScheduler();
        // 3、创建jobDetail   将job builder进jobdetail
        JobDetail jobDetail = JobBuilder.newJob(RamJob.class).withDescription("this is a ram job")
                                .withIdentity("ramJob2","ramGroup2").build();

        // 4、创建Trigger 使用simpleScheduleBuild 或者 CronScheduleBuild
        Date startTime = new Date(System.currentTimeMillis() + 3*1000);
        Trigger trigger = TriggerBuilder.newTrigger().withDescription("")
                            .withIdentity("ramTriggerName2","ramTriggerName2")
                            .startAt(startTime) // 默认启动时间
                            .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))// 每隔5s执行一次
                            .build();

        // 注册任务触发器
        scheduler.scheduleJob(jobDetail,trigger);
        scheduler.start();
        System.out.println("scheduler启动时间" + new Date());
        Thread.sleep(60000);
        scheduler.shutdown();
    }
}
