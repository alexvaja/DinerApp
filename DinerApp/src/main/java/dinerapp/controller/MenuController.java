package dinerapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dinerapp.entity.Category;
import dinerapp.repository.CategoryRepository;
import dinerapp.repository.FoodRepository;

@Controller
public class MenuController {
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	FoodRepository foodRepository;

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
			System.out.println("Am intrat in AddCategory");
			addMenuIsAvailable = true;
			model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
			addCategoryIsAvailable = true;
			model.addAttribute("addCategoryIsAvailable", addCategoryIsAvailable);
			
			List<Category> categoryList = new ArrayList<>(getListOfCategory());
			model.addAttribute("categoryList", categoryList);
			
			break;
		}
		return "menuView";
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
