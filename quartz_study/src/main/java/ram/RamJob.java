package ram;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * @Auther: lvla
 * @Date: 2018/12/22 10:38
 * @Description: 创建job
 */
public class RamJob implements Job {

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(new Date() + "====定时job，执行任务====");
    }
}
