package dinerapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import dinerapp.entity.Category;
import dinerapp.model.CategoryViewModel;
import dinerapp.repository.CategoryRepository;

@Controller
public class CategoryController {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@GetMapping("/allCategories")
	public String getAllCategories(Model model) {
		System.out.println("getAllCategories");
		
		CategoryViewModel categoryViewModel = new CategoryViewModel();
		categoryViewModel.setCategoryItems(getListOfCategory());
		model.addAttribute("categoryViewModel", categoryViewModel);
		return "allCategories";
	}
	
	@GetMapping("/allCategories/addCategory")
	public String createCategory(Model model) {
    	System.out.println("createCategory");.
    	
		model.addAttribute("categoryViewModel", new Category());
		return "editCategory";
	}
	
	@PostMapping("allCategories")
	public String saveCategory(Model model, @ModelAttribute Category category) {
    	System.out.println("saveCategory");
    	System.out.println(category);
    	
    
		categoryRepository.save(category);
		return "redirect:/";
	}
	
	private List<Category> getListOfCategory() {
		
		Iterable<Category> list = categoryRepository.findAll();
		List<Category> searchedList= new ArrayList<>();
		for (Category category : list) {
			searchedList.add(category);
		}
		return searchedList;
	}
}