package dinerapp.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController 
{

	@RequestMapping("/error")
	public String renderErrorPage(Model model, HttpServletRequest httpRequest) 
	{
		Object status = httpRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if (status != null) 
		{
			Integer statusCode = Integer.valueOf(status.toString());
			if(statusCode == HttpStatus.NOT_FOUND.value())
				return "error/admin/A404Error";
			if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value())
				return "error/admin/A500Error";
		}
		return "error/adminError";
	}
	
	@Override
	public String getErrorPath() {
		return "/adminError";
	}
}