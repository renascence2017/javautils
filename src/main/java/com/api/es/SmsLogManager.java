package com.api.es;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

public class SmsLogManager {


    private Integer maxQueueSize = 10240;


    private Integer esThreadSize = 2;


    @Autowired
    private ESSearchService esSearchService;

    private LinkedBlockingQueue<SmsLogDTO> smsInsertQueue;

    private LinkedBlockingQueue<SmsLogDTO> smsUpdateQueue;

    private ExecutorService threadPool;

    @PostConstruct
    public void init() {

        //实时数据监控和上报管理
        smsInsertQueue = new LinkedBlockingQueue<SmsLogDTO>(maxQueueSize);
        //实时数据监控和上报管理
        smsUpdateQueue = new LinkedBlockingQueue<SmsLogDTO>(maxQueueSize);
        // 创建一个可重用固定线程数的线程池
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("SmsLogRecord-pool-%d").build();
        int threadSize = /*monKafkaThreadSize + reqKafkaThreadSize +*/ esThreadSize;
        threadPool = new ThreadPoolExecutor(threadSize, threadSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(threadSize), threadFactory);

        //实时监测es入库
        threadPool.submit(new EsInsertThread(smsInsertQueue, esSearchService));
        threadPool.submit(new EsUpdateThread(smsUpdateQueue, esSearchService));

    }

    /**
     * 队列满了打出错误日志 不影响业务
     *
     * @param smsLogDTO
     */
    @Override
    public void save(SmsLogDTO smsLogDTO) {
        try {
            smsInsertQueue.put(smsLogDTO);
        } catch (Exception e) {
            log.error("save smsLog error!", e);
        }
    }

    /**
     * 队列满了打出错误日志 不影响业务
     *
     * @param smsLogDTO
     */
    @Override
    public void update(SmsLogDTO smsLogDTO) {
        try {
            smsUpdateQueue.put(smsLogDTO);
        } catch (Exception e) {
            log.error("update smsLog error!", e);
        }
    }

}
