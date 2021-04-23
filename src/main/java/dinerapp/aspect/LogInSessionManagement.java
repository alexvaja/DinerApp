package dinerapp.aspect;

import java.security.SecureRandom;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogInSessionManagement {
	
	private Boolean sessionExist = false;
	
	private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	public static final int SECURE_TOKEN_LENGTH = 256;

	private static final SecureRandom random = new SecureRandom();

	private static final char[] symbols = CHARACTERS.toCharArray();

	private static final char[] buf = new char[SECURE_TOKEN_LENGTH];
	
	private String SECURE_TOKEN;
	
	@Before("execution (* dinerapp.controller.IndexController..*(..)) &&" + "args(session,..)")
	public void beforeIndexController(HttpSession session, JoinPoint joinPoint) {
		System.out.println("Join Point: " + joinPoint.getSignature().toShortString());
		System.out.println("Session ID: " + session.getId() + " " + session.getCreationTime());
		
		if (session != null) {
			sessionExist = true;
		}	
	}
	
	@After("execution (* dinerapp.controller.IndexController..*(..)) &&" + "args(session,..)")
	public void afterIndexController(HttpSession session, JoinPoint joinPoint) {
		System.out.println("Join Point " + joinPoint.getSignature().toShortString());
		System.out.println("After Login " + session.getId());
		
		SECURE_TOKEN = nextToken();
		
		if (session != null && sessionExist) {
			session.setAttribute("SECURE_TOKEN", SECURE_TOKEN);
		}
	}
	
	private String nextToken() {
	    for (int idx = 0; idx < buf.length; ++idx)
	        buf[idx] = symbols[random.nextInt(symbols.length)];
	    return new String(buf);
	}
}
