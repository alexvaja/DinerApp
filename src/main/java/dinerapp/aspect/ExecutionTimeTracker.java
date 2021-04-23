package dinerapp.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTimeTracker {
	private long startTime;
	
	@Before("execution (* dinerapp.controller..*(..))")
	public void beforeControllerMethodCalled() {
		startTime = System.nanoTime();
	}
	
	@After("execution (* dinerapp.controller..*(..))")
	public void afterControllerMethodCalled(JoinPoint joinPoint) {
		long endTime = System.nanoTime();
		System.out.println("Execution time from " + joinPoint.getSignature().toShortString() + ": " + (endTime - startTime) / 10000 + " msec.");
	}
}
