package dinerapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DinerAppController {

	@GetMapping("/dinerapp")
	public String getDinerAppForm(Model model) {
		return "dinerapp";
	}
	
	@GetMapping("/")
	public String getDinerAppForm2(Model model) {
		return "dinerapp";
	}
}
