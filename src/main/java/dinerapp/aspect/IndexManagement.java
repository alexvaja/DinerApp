package dinerapp.aspect;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Aspect
@Component
public class IndexManagement {
	
	@Before("execution (* dinerapp.controller.DinerAppController..*(..)) &&" + "args(session, model, principal)")
	public void beforeDinerAppController(HttpSession session, Model model, Principal principal) {
		
		if (session != null) {
			session.invalidate();
		}
	}
	
	@After("execution (* dinerapp.controller.DinerAppController..*(..)) &&" + "args(session, model, principal)")
	public void afterDinerAppController(HttpSession session, Model model, Principal principal) {
		System.out.println("2");
	}
}
