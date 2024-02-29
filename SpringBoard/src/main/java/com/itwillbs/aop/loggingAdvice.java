package com.itwillbs.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

// log출력객체 - Advice
public class loggingAdvice implements MethodInterceptor{

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		System.out.println(" 보조기능 : LoggingAdvice 실행 ");
		System.out.println(" 주기능 "+invocation.getMethod()+" 실행 전 ");
		
		// 주 기능 호출
		Object obj = invocation.proceed();
		
		System.out.println(" 주기능 "+invocation.getMethod()+" 실행 후 ");
		System.out.println(" 보조기능 : LoggingAdvice 실행 끝");
		
		return obj;
	}
	
}
