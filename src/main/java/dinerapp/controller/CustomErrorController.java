package dinerapp.controller;

import java.security.Principal;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import dinerapp.model.entity.Role;
import dinerapp.model.entity.UserDiner;
import dinerapp.repository.UserCantinaRepository;

@Controller
public class CustomErrorController implements ErrorController {

	@Autowired
	private UserCantinaRepository userRepo;

	@Override
	public String getErrorPath() {
		return "/adminError";
	}

	@RequestMapping("/error")
	public String renderErrorPage(Model model, HttpServletRequest httpRequest, Principal principal,
			HttpSession session) 
	{
		Role role = new Role();

		UserDiner user = this.getUserByName(session.getAttribute("nameFromURL").toString());

		System.out.println("USER: " + user.toString());
		Object status = httpRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		Integer statusCode = Integer.valueOf(status.toString());
		
		System.out.println(principal);
		
		if (principal != null && status != null) 	
			{
				role = this.getUsersByRole(principal.getName());

				if (role.getName().equals("admin"))
				{
					System.out.println("Am intrat in admin");
					if (statusCode == HttpStatus.NOT_FOUND.value())
						return "error/admin/A404Error";
					if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value())
						return "error/admin/A500Error";
				}
			}
		else if (user != null) 
			{
				System.out.println("Am intrat in emplouyee");
				if (statusCode == HttpStatus.NOT_FOUND.value())
					return "error/employee/E404Error";
				if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value())
					return "error/employee/E500Error";
			}	
		return "error/adminError";
	}

	private Role getUsersByRole(String name) {
		Iterable<UserDiner> users = userRepo.findAll();
		for (UserDiner user : users)
			if (user.getName().equals(name))
				return user.getRoles().get(0);
		return null;
	}

	private UserDiner getUserByName(String name) {
		Iterable<UserDiner> users = userRepo.findAll();

		for (UserDiner user : users) {
			if (user.getName().equals(name)) {
				return user;
			}
		}
		return null;

	}
}