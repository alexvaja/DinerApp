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
		UserDiner employeeUser = null;

		if (session.getAttribute("nameFromURL") != null) {
			employeeUser = this.getUserByName(session.getAttribute("nameFromURL").toString());
			System.out.println("Userul de pe sesiune este " + employeeUser.getName());
		}

		Object status = httpRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		Integer statusCode = Integer.valueOf(status.toString());
		
		if(status != null)
		{
			if(principal != null && employeeUser == null)
			{				
				System.out.println("Am intrat in admin");
				if (statusCode == HttpStatus.NOT_FOUND.value())
					return "error/admin/A404Error";
				if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value())
					return "error/admin/A500Error";
			}
			else if(principal == null && employeeUser != null)
			{
				System.out.println("Am intrat in employee");
				if (statusCode == HttpStatus.NOT_FOUND.value())
					return "error/employee/E404Error";
				if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value())
					return "error/employee/E500Error";
			}
			else if(principal != null && employeeUser != null)
			{
				System.out.println("Sunt logat si ca admin si ca angajat");
				if(statusCode == HttpStatus.NOT_FOUND.value())
					return "error/general404Error";
				if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value())
					return "error/general500Error";					
			}
		}
		return "error/adminError";
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