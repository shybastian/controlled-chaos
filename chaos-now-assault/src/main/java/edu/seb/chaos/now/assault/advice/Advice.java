package edu.seb.chaos.now.assault.advice;

import edu.seb.chaos.now.assault.components.ExceptionAssaultRequestScope;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

@RequiredArgsConstructor
public class Advice implements MethodInterceptor {
    private final ExceptionAssaultRequestScope requestScope;
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        MethodInvocationProceedingJoinPoint pjp =
                new MethodInvocationProceedingJoinPoint((ProxyMethodInvocation) invocation);
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        requestScope.doSomeChaos(methodSignature.getName());
        return pjp.proceed();
    }
}
