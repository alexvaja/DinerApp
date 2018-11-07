package dinerapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MenuController {

	@GetMapping("/menuView")
	public String getAllMenu(Model model) {
		Boolean addMenuIsAvailable = false;
		model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
		return "menuView";
	}
	
	@PostMapping("/menuView")
	public String setAllMenu(Model model, @RequestParam("submit") String reqParam) {
		System.out.println(reqParam);
		Boolean addMenuIsAvailable = false;
		Boolean addCategoryIsAvailable = false;
		model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
		
		switch(reqParam) {
		case "AddMenu":
			addMenuIsAvailable = true;
			model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
			break;
		case "AddCategory":
			addMenuIsAvailable = true;
			model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
			addCategoryIsAvailable = true;
			model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
			break;
		}
		return "menuView";
	}
}
