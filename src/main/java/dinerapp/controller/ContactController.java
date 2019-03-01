package dinerapp.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

import dinerapp.exceptions.NewSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Controller
public class ContactController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@ExceptionHandler({ NewSessionException.class })
	public String sessionError() {
		LOGGER.error("incercare de acces nepermis");
		
		return "views/loginView";
	}
	
	@GetMapping("/contactView")
	public String getForm(Model model, HttpSession session) throws NewSessionException {
		
		if (session.isNew()) {
			throw new NewSessionException();			
		}
		
		return "contactView";
	}

}
