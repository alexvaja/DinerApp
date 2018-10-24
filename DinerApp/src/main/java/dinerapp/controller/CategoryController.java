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
	
//	@GetMapping("/all")
//	public String showAll(Model model) {
//	    model.addAttribute("books", bookService.findAll());
//	    return "books/allBooks";
//	}
	
	
	@GetMapping("/allCategory")
	public String getForm(Model model) {
		System.out.println("Ajung in get category");//
		
		CategoryViewModel categoryViewModel = new CategoryViewModel();
		//System.out.println(categoryViewModel);//
		//System.out.println(getListOfCategory());//
		categoryViewModel.setCategoryItems(getListOfCategory());
		//System.out.println(categoryViewModel);//
		model.addAttribute("categoryViewModel", categoryViewModel);
		return "allCategory";
	}
	
	@PostMapping("/categoryView")
	public String getCategoryList(@ModelAttribute CategoryViewModel categoryViewModel) {
		System.out.println("Ajung in post category");//
		
		List<Category> searchedList= new ArrayList<>(getListOfCategory());
		searchedList.add(new Category());
		
		categoryViewModel.setCategoryItems(searchedList);
		System.out.println(categoryViewModel);
		return "categoryView";
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