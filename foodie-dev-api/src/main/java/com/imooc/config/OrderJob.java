package com.imooc.config;


import com.imooc.service.OrderService;
import com.imooc.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时扫描超时未支付订单
 */
@Component
public class OrderJob {
    @Autowired
    private OrderService orderService;
    // 需要在启动类里用@EnableScheduling 启动
    @Scheduled(cron = "0/10 * * * * ? ")
    public void autoCloseOrder(){
        System.out.println("定时任务执行时间：" + DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
        orderService.closeOrder();
    }
}