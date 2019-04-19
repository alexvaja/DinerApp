package dinerapp.controller;

import java.security.Principal;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

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

	@RequestMapping("/error")
	public String renderErrorPage(Model model, HttpServletRequest httpRequest, Principal principal) 
	{
		Role role = this.getUsersByRole(principal.getName());
		Object status = httpRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if (status != null) 
		{
			Integer statusCode = Integer.valueOf(status.toString());
			if (role.getName().equals("admin")) 
			{
				if (statusCode == HttpStatus.NOT_FOUND.value())
					return "error/admin/A404Error";
				if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value())
					return "error/admin/A500Error";
			}
			else if(role.getName().equals("employee"))
			{
				if (statusCode == HttpStatus.NOT_FOUND.value())
					return "error/admin/E404Error";
				if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value())
					return "error/admin/E500Error";
			}
		}
		return "error/adminError";
	}

	@Override
	public String getErrorPath() {
		return "/adminError";
	}

	private Role getUsersByRole(String name) {
		Iterable<UserDiner> users = userRepo.findAll();
		for (UserDiner user : users) {
			if (user.getName().equals(name))
				return user.getRoles().get(0);

		}
		return null;
	}
}