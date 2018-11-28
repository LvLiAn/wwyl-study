package com.wwyl.study.netty_study.io.bio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: lvla
 * @Date: 2018/11/16 14:55
 * @Description: 处理 客户端连接 线程池
 */
public class TimeServerHandlerExecPool {

    private ExecutorService executorService;

    public TimeServerHandlerExecPool(int maxSize,int queueSize) {
        /**
         * int corePoolSize,  线程池最小数量
         * int maximumPoolSize,线程池最大数量
         * long keepAliveTime,
         * TimeUnit unit,
         * BlockingQueue<Runnable> workQueue
         */
        this.executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),maxSize,120l, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void exec(Runnable task){
        executorService.execute(task);
    }
}
