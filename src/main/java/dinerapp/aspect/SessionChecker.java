package dinerapp.aspect;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import dinerapp.exceptions.NewSessionException;

@Aspect
@Component
public class SessionChecker {
	
	String SECURE_TOKEN = null;

	@Before("execution (* dinerapp.controller..*(..)) &&" + "args(session, ..)")
	public void beforeDinerAppController(HttpSession session) throws NewSessionException {
		
		
		if (session != null) {
			System.out.println("Session Checker");
			
			//SECURE_TOKEN = (String) session.getAttribute("SECURE_TOKEN");
			
			//Check the SECURE_TOKEN and if it nit exist, destroy the session
		}
	}
	
	@After("execution (* dinerapp.controller..*(..)) &&" + "args(session, ..)")
	public void afterDinerAppController(HttpSession session) throws NewSessionException {
		
		if (session != null) {
			System.out.println("Session Checker");
			
			//SECURE_TOKEN = (String) session.getAttribute("SECURE_TOKEN");
			
			//Check the SECURE_TOKEN and if it nit exist, destroy the session
		}
	}
}
