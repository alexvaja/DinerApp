package dinerapp.aspect;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Aspect
@Component
public class IndexManagement {
	
	private HttpServletRequest request;
	
	@Before("execution (* dinerapp.controller.DinerAppController..*(..)) &&" + "args(session, model, principal)")
	public void beforeIndexController(HttpSession session, Model model, Principal principal) {
		System.out.println("1");
		
		if (session != null) {
			session.invalidate();
		}
		
	}
	
	@After("execution (* dinerapp.controller.DinerAppController..*(..)) &&" + "args(session, model, principal)")
	public void afterIndexController(HttpSession session, Model model, Principal principal) {
		System.out.println("2");
		
		//session = request.getSession(true);
	}
}
