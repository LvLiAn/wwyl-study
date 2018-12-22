package springjob;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * @Auther: lvla
 * @Date: 2018/12/22 14:46
 * @Description:
 */
public class HelloWorldJob implements Job {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(new Date() + "===spring job statr===");
    }
}
