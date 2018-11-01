package dinerapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dinerapp.entity.Category;
import dinerapp.model.CategoryViewModel;
import dinerapp.repository.CategoryRepository;

@Controller
public class CategoryController {

	@Autowired
	private CategoryRepository categoryRepository;
	@GetMapping("/categoryView")
	public String getAllCateogires(Model model)
	{
		model.addAttribute("categoryViewModel", new CategoryViewModel(getListOfCategory()));
		//model.addAttribute("category", new Category());
		System.out.println(getListOfCategory());
		return "categoryView";
	}
	@PostMapping("/categoryView")
	public String setAllCategories(Model model, @ModelAttribute Category category, @RequestParam("submit") String reqParam)
	{
		System.out.println("setAllCategories");
		
		Boolean addCategoryIsAvailable = false;
		model.addAttribute("addCategoryIsAvailable", addCategoryIsAvailable);
		model.addAttribute("categoryViewModel", new CategoryViewModel(getListOfCategory()));
		model.addAttribute("category", new Category());
		System.out.println(category);
		System.out.println(reqParam);
		switch(reqParam)
		{
			case "Add":
				addCategoryIsAvailable = true;
				model.addAttribute("addCategoryIsAvailable", addCategoryIsAvailable);
				break;
			case "Save":
				categoryRepository.save(category);
				addCategoryIsAvailable = false;
				model.addAttribute("addCategoryIsAvailable", addCategoryIsAvailable);
				model.addAttribute("categoryViewModel", new CategoryViewModel(getListOfCategory()));
				break;
			case "Cancel":
				break;
		}
		return "categoryView";
	}
//	@GetMapping("/allCategories")
//	public String getAllCategories(Model model) {
//		System.out.println("getAllCategories");
//		
//		CategoryViewModel categoryViewModel = new CategoryViewModel();
//		categoryViewModel.setCategoryItems(getListOfCategory());
//		model.addAttribute("categoryViewModel", categoryViewModel);
//		return "allCategories";
//	}
//	
//	@GetMapping("/allCategories/addCategory")
//	public String createCategory(Model model) 
//	{
//    	System.out.println("createCategory");
//    	
//		model.addAttribute("categoryViewModel", new Category());
//		return "editCategory";
//	}
//	
//	@GetMapping("/allCategories/editCategory/{id}")
//	public String editCategory(Model model, @PathVariable(value = "id") Integer id) {
//		model.addAttribute("categoryViewModel", categoryRepository.findById(id));
//		return "editCategory";
//	}
//	
//	@GetMapping("/allCategories/deleteCategory/{id}")
//	public String deleteCategory(Model model, @PathVariable(value = "id") Integer id) {
//		categoryRepository.deleteById(id);
//		return "redirect:/allCategories";
//	}
//	
//	@PostMapping("/allCategories")
//	public String saveCategory(Model model, @ModelAttribute Category category) {
//    	System.out.println("saveCategory");
//    	System.out.println(category);
//		categoryRepository.save(category);
//		return "redirect:/allCategories";
//	}
//	
	private List<Category> getListOfCategory() {
		
		Iterable<Category> list = categoryRepository.findAll();
		List<Category> searchedList= new ArrayList<>();
		for (Category category : list) {
			searchedList.add(category);
		}
		return searchedList;
	}
}