package jdbc;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.List;

/**
 * @Auther: lvla
 * @Date: 2018/12/22 13:42
 * @Description:
 */
public class JdbcQuartz {

    public static void main(String[] args) {
        try {
            startScheduler();
//            resumeJob();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startScheduler() throws Exception {
        JobDetail jobDetail = JobBuilder.newJob(JdbcJob.class)
                                .withDescription("")
                                .withIdentity("job1_1","jobGroup")
                                .build();

        // 触发器类型 执行5次
        SimpleScheduleBuilder builder = SimpleScheduleBuilder.repeatSecondlyForTotalCount(5);

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1_1","tGroup1").startNow()
                        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(5))
                        .build();


        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.scheduleJob(jobDetail,trigger);
        scheduler.start();

        try{
            Thread.sleep(60000);
        }catch (Exception e){
            e.printStackTrace();
        }
        // 关闭定时器
        scheduler.shutdown();
        System.out.println("==jdbc job  shutDown==");
    }

    private static void resumeJob() throws Exception{
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        JobKey jobKey = new JobKey("job1_2","jobGroup");
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
        if(triggers.size() > 0){
            for (Trigger tg : triggers) {
                // 根据类型判断
                if ((tg instanceof CronTrigger) || (tg instanceof SimpleTrigger)) {
                    // 恢复job运行
                    scheduler.resumeJob(jobKey);
                }
            }
            scheduler.start();

        }

    }
}
