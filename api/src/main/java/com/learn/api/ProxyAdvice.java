package com.learn.api;

import com.learn.api.elements.Element;
import com.learn.api.elements.MethodElement;
import com.learn.api.extractor.MethodExtractor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ProxyAdvice {

    Map<String, MethodElement> methods = new HashMap<>();

    @Around("com.learn.api.annotationProcessing.CommonPointcuts.proxy()")
    public Object track(ProceedingJoinPoint pjp) {
        try {
            MethodElement methodElement = getMethodElement(pjp);
            return pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    private MethodElement getMethodElement(ProceedingJoinPoint pjp) {
        Method method = getMethod(pjp);
        assert method != null;
        String methodKey = Integer.toString(method.hashCode());
        if(!methods.containsKey(methodKey))
           methods.put(methodKey, MethodExtractor.extract(method));
        return methods.get(methodKey);
    }

    private Method getMethod(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        try {
            method = pjp.getTarget().getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
            return method;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
