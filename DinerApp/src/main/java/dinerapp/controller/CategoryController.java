package dinerapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import dinerapp.repository.CategoryRepository;

@Controller
@RequestMapping("/category")
public class CategoryController {

	private CategoryRepository categoryRepository;
	
	@GetMapping("/category")
	public String getCategoryList(Model model) {
		
		
		return "category";
	}
	
	@GetMapping("/category")
	public String getForm(Model model) {
		return "category";
	}
}
