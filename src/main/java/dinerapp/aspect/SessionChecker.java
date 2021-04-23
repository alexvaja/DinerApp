package dinerapp.aspect;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SessionChecker {
	
	String SECURE_TOKEN = null;

	@Before("execution (* dinerapp.controller..*(..)) &&" + "args(session, ..)")
	public void beforeDinerAppController(HttpSession session) {
		
		
		if (session != null) {
			System.out.println("Session Checker");
			
			SECURE_TOKEN = (String) session.getAttribute("SECURE_TOKEN");
			
			if (SECURE_TOKEN == null) {
				session.invalidate();
			}
		}
	}
	
	@After("execution (* dinerapp.controller..*(..)) &&" + "args(session, ..)")
	public void afterDinerAppController(HttpSession session) {
		
		if (session != null) {
			System.out.println("Session Checker");
			
			SECURE_TOKEN = (String) session.getAttribute("SECURE_TOKEN");
			
			if (SECURE_TOKEN == null) {
				session.invalidate();
			}
		}
	}
}
