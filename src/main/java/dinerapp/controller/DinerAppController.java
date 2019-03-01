package dinerapp.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.annotation.SessionScope;

import dinerapp.security.config.WebUtils;

@Controller
@RequestMapping("/")
public class DinerAppController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage(Model model, Principal principal) {
		LOGGER.info("am intrat mai ceva ca Hitler in Polonia cu panzerul");
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		String userInfo = WebUtils.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);
		return "adminPage";
	}

	@SessionScope
	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public String loginPage(Model model, Principal principal) {
		return "views/loginView";
	}

	@RequestMapping(value = { "/expired" }, method = RequestMethod.GET)
	public String sessionExpiredPage(Model model, Principal principal) {
		return "views/sessionTimeoutView";
	}

	@RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
	public String logoutSuccessfulPage(Model model) {
		return "views/loginView";
	}

//	@RequestMapping(value = { "/userAccountInfo" }, method = RequestMethod.GET)
//	public String userInfo(Model model, Principal principal) {
//		
//		String userName = principal.getName();
//		System.out.println("User Name: " + userName);
//		User loginedUser = (User) ((Authentication) principal).getPrincipal();
//		String userInfo = WebUtils.toString(loginedUser);
//		model.addAttribute("userInfo", userInfo);
//		return "index";
//	}
}
