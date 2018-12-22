package springjob;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Auther: lvla
 * @Date: 2018/12/22 15:01
 * @Description:
 */
public class SpringRamQuartzTest {
    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring_ram_quartz.xml");

    }
}
