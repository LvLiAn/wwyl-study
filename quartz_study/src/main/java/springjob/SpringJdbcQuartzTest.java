package springjob;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Auther: lvla
 * @Date: 2018/12/22 15:05
 * @Description:
 */
public class SpringJdbcQuartzTest {

    private static Scheduler scheduler;
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring_jdbc_quartz.xml");
        scheduler = (Scheduler)applicationContext.getBean("scheduler");
        try {
            startScheduler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startScheduler() throws Exception{

        JobDetail jobDetail = JobBuilder.newJob(HelloWorldJob.class)
                                .withIdentity("sj_job","sj_group")
                                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                            .withIdentity("sj_trigger","sj_tgroup")
                            .startNow()
                            .withSchedule(CronScheduleBuilder.cronSchedule("0/3 * * * * ?"))
                           .build();

        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        // 4、调度执行
        scheduler.scheduleJob(jobDetail, trigger);


    }

}
