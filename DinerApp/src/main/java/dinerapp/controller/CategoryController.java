package dinerapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import dinerapp.entity.Category;
import dinerapp.model.CategoryViewModel;
import dinerapp.repository.CategoryRepository;

@Controller
public class CategoryController {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@GetMapping("/categoryView")
	public String getForm(Model model) 
	{
		System.out.println("am intrat in categorie");
		model.addAttribute("categoryViewModel", new CategoryViewModel());
		return "categoryView";
	}
	@PostMapping("/categoryView")
	public String getCategoryList(Model model) 
	{	
		List<Category> categoryList = (List<Category>) categoryRepository.findAll();
		model.addAttribute("categoryViewModel", new CategoryViewModel((List<Category>) categoryRepository.findAll()));
		return "categoryView";
	}
}
