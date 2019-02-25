package dinerapp.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import dinerapp.exceptions.NewSessionException;

@Controller
public class IndexController 
{
	@GetMapping("index")
	public String getIndex(Model model, HttpSession session) throws NewSessionException
	{
		return "index";
	}
}
