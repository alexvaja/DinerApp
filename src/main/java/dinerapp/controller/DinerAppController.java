package dinerapp.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import dinerapp.security.config.*;
@Controller
@RequestMapping("/")
public class DinerAppController 
{
//	@GetMapping("/index")
//	public String getDinerAppForm(Model model) {
//		System.out.println("am intrat in cantina");
//		return "index";
//	}
//	@GetMapping("/")
//	public String getDinerAppForm2(Model model) {
//		System.out.println("am intrat in cantina2");
//		return "index";
//	}	
	@PostMapping("/dinerapp")
	public String ceva(@RequestParam("submit") String reqParam)
	{
		System.out.println("am intrat in post");
		System.out.println(reqParam);
		switch(reqParam) {
		case "food":
			System.out.println("am intrat in cantina3");
			return "foodView";
		}
		return "dinerapp";
	}
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {
         
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);    
        return "adminPage";
    }
    
    @SessionScope
    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public String loginPage(Model model,Principal principal) {
        return "loginView";
    }
    
    @RequestMapping(value = { "/expired"}, method = RequestMethod.GET)
    public String sessionExpiredPage(Model model,Principal principal) {
        return "sessionTimeoutView";      
    }

    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model) {        
        return "loginView";
    }
 
    @RequestMapping(value = { "/userAccountInfo"}, method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {
        String userName = principal.getName(); 
        System.out.println("User Name: " + userName);
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
        return "index";
   }   
}
