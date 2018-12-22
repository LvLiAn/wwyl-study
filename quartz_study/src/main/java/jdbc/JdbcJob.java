package jdbc;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * @Auther: lvla
 * @Date: 2018/12/22 13:41
 * @Description:
 */
public class JdbcJob implements Job {
    public void execute(JobExecutionContext jobExecutionContext)  {
        System.out.println(new Date() + "====定时JDBC job，执行任务====");
    }
}
