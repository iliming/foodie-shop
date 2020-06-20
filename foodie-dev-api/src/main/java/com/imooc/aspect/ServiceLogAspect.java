package com.imooc.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component //组件注解 需要被扫描
@Aspect //切面注解
public class ServiceLogAspect {
    public static  final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);
    /**
     * Aop通知：
     * 1. 前置通知：方法调用执行前
     * 2. 后置通知：方法正常调用后
     * 3. 环绕通知：方法调用前后,都分别可以执行的通知
     * 4. 异常通知：在方法调用过程中发生异常，则通知
     * 5. 最终通知：方法调用之后执行
     */


    /**
     * 切面表达式：
     * execution 代表所要执行的的表达式主体
     * 第一处 * 代表方法返回类型， * 代表任何类型
     * 第二处 包名代表aop监控的类所在的包
     * 第三处 .. 代表该包以及子包下的所有类方法
     * 第四处 * 代表类名，* 代表所有类
     * 最终 .*.*代表任何类，任何方法 (..)任意参数
     *
     * @param joinPoint 切入点
     * @return object
     * @throws Throwable throw
     */
    @Around("execution(* com.imooc.service.impl..*.*(..))")  //所有符合切面表达式额地方都出现通知
    public Object recordTimeLog(ProceedingJoinPoint joinPoint)throws Throwable{
        logger.info("======  开始执行{}.{}  ======",  //占位符 某一个类的某一个方法
                joinPoint.getTarget().getClass(),joinPoint.getSignature().getName());
        //记录开始时间
        long begin = System.currentTimeMillis();
        //记录目标 service
        Object result = joinPoint.proceed();
        //记录结束时间
        long end = System.currentTimeMillis();
        long takeTime = end -begin;
        if(takeTime>3000){
            logger.error("======  执行结束，耗时：{} 毫秒  ======",takeTime);
        }else if(takeTime>2000){
            logger.warn("======  执行结束，耗时：{} 毫秒  ======",takeTime);
        }else {
            logger.info("======  执行结束，耗时：{} 毫秒  ======",takeTime);
        }
        return result;

    }
}
