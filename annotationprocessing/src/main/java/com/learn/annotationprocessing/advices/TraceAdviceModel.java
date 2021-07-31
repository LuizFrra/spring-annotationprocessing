package com.learn.annotationprocessing.advices;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.StopWatch;

public class TraceAdviceModel {
    public Object track(ProceedingJoinPoint pjp) {
        StopWatch sw = new StopWatch(getClass().getSimpleName());
        try {
            sw.start(pjp.getSignature().toShortString());
            return pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            sw.stop();
            System.out.println(sw.prettyPrint());
        }
        return null;
    }
}
