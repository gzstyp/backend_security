package com.fwtai.service;

import com.fwtai.dao.AsyncDao;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步处理,注意，这里的异步方法，只能在本类的自身之外调用，在本类调用是无效的!!!,即在别的类调用即可
 * @注意 需要在启动类添加开启异步处理@EnableAsync注解类
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-04-10 20:11
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Component
public class AsyncService{

    @Resource
    private AsyncDao asyncDao;

    // 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程,
    // 但是这方式会使线程池为无限大，当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程。
    @Async
    public void updateLogin(final String userId){
        final ExecutorService threadPool = Executors.newCachedThreadPool();
        threadPool.execute(new Runnable(){
            @Override
            public void run(){
                try {
                    asyncDao.updateSucceed(userId);
                } catch (Exception e) {
                    threadPool.shutdown();
                }
            }
        });
    }

    /*
        创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
        因为线程池大小为3，每个任务输出index后sleep 2秒，所以每两秒打印3个数字。
        定长线程池的大小最好根据系统资源进行设置。如Runtime.getRuntime().availableProcessors()。可参考PreloadDataCache。
     */
    protected void executeNewFixedThreadPool(){
        final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            fixedThreadPool.execute(new Runnable(){
                @Override
                public void run() {
                    try {
                        System.out.println(index);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /*
        创建一个定长线程池，支持定时及周期性[定期执行]任务执行,表示延迟3秒执行。
    */
    protected void executeNewScheduledThreadPool(){
        //定时执行
        final ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        scheduledThreadPool.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("延迟3秒执行");
            }
        }, 3,TimeUnit.SECONDS);

        //周期性[定期]执行任务,表示延迟1秒后每3秒执行一次。ScheduledExecutorService比Timer更安全，功能更强大
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("表示延迟1秒后每3秒执行一次");
            }
        }, 1,3,TimeUnit.SECONDS);
    }

    /*
        创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
    */
    protected void executeNewSingleThreadExecutor(){
        final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(index);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}