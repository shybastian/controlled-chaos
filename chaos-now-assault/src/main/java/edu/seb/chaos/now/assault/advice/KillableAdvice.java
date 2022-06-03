package edu.seb.chaos.now.assault.advice;

import edu.seb.chaos.now.assault.assaults.KillAssault;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

@RequiredArgsConstructor
public class KillableAdvice implements MethodInterceptor {
    private final KillAssault killAssault;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        MethodInvocationProceedingJoinPoint pjp =
                new MethodInvocationProceedingJoinPoint((ProxyMethodInvocation) invocation);
        killAssault.attack();
        return pjp.proceed();
    }
}
