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
	
	@GetMapping("/categoryView")
	public String getForm(Model model) {
		System.out.println("Ajung in get");
		CategoryViewModel categoryViewModel = new CategoryViewModel();
		Iterable<Category> list = categoryRepository.findAll();
		List<Category> searchedList= new ArrayList<>();
		for (Category category : list) {
			searchedList.add(category);
			System.out.println(category);
		}
		
		categoryViewModel.setCategoryItems(searchedList);
		model.addAttribute("categoryViewModel", categoryViewModel);
		return "categoryView";
	}
	
	@PostMapping("/categoryView")
	public String getCategoryList(@ModelAttribute CategoryViewModel categoryViewModel) {
		Iterable<Category> list = categoryRepository.findAll();
		List<Category> searchedList= new ArrayList<>();
		for (Category category : list) {
			searchedList.add(category);
			System.out.println(category);
		}
		searchedList.add(new Category());
		System.out.println();
		
		
		
		categoryViewModel.setCategoryItems(searchedList);
		return "categoryView";
	}
	
}
