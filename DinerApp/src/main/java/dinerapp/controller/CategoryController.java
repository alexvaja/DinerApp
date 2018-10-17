package dinerapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import dinerapp.model.CategoryViewModel;

@Controller
@RequestMapping("/categoryView")
public class CategoryController 
{
	@GetMapping("/categoryView")
	public String getForm(Model model) 
	{
		return "category";
	}
	
	@PostMapping("/categoryView")
	public String openCategoryView(@ModelAttribute CategoryViewModel categoryViewModel)
	{
		return "categoryView";
	}
}
