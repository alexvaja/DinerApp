package dinerapp.aspect;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.ui.Model;

@Aspect
public class DinerAppControllerAspect {
	
	@Pointcut("execution (* dinerapp.controller.DinerAppController.adminPage(javax.servlet.http.HttpSession, "
			+ "org.springframework.ui.Model, java.security.Principal)) && args(model, principal)")
	public void adminPage(Model model, Principal principal) {
		
	}

	@Pointcut("execution (* dinerapp.controller.DinerAppController.loginPage(javax.servlet.http.HttpSession, "
			+ "org.springframework.ui.Model, java.security.Principal)) && args(session, model, principal)")
	public void loginPage(HttpSession session, Model model, Principal principal) {
		
	}

	@Pointcut("execution (* dinerapp.controller.DinerAppController.sessionExpiredPage(javax.servlet.http.HttpSession, "
			+ "org.springframework.ui.Model, java.security.Principal)) && args(session, model, principal)")
	public void sessionExpiredPage(HttpSession session, Model model, Principal principal) {
		
	}

	@Pointcut("execution (* dinerapp.controller.DinerAppController.logoutSuccessfulPage(org.springframework.ui.Model, "
			+ "java.security.Principal)) && args(session, model, principal)")
	public void logoutSuccessfulPage(HttpSession session, Model model, Principal principal) {
		
	}
}
