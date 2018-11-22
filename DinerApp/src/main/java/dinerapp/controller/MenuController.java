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
import dinerapp.entity.Food;
import dinerapp.model.MenuViewModel;
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
	int number = 0;
	@PostMapping("/menuView")
	public String setAllMenu(Model model, @RequestParam("submit") String reqParam, 
										  @ModelAttribute("menuViewModel") MenuViewModel menuViewModel2) {
		System.out.println(reqParam);
		MenuViewModel menuViewModel = new MenuViewModel();
		Boolean addMenuIsAvailable = false;
		model.addAttribute("menuViewModel", menuViewModel);
		model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
		model.addAttribute("categoryList", getListOfCategory());
		model.addAttribute("foodList", getlistOfFood());
		model.addAttribute("number", number);
		System.out.println(number);
		//BAG PULA IN EL MENIU DACA NU MA HRANESC ASTIA PE MOCA APOI
		
		switch(reqParam) {
		case "AddMenu":
			System.out.println("Am intart in AddMenu");
			
			addMenuIsAvailable = true;
			model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
			number++;
			System.out.println(number);
			model.addAttribute("number", number);
			break;
		}
		System.out.println("uite modelul poate:" + menuViewModel2);
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
	
	private List<Food> getlistOfFood() {
		Iterable<Food> list = foodRepository.findAll();
		List<Food> searchedList = new ArrayList<>();
		for (Food food : list) {
			searchedList.add(food);
		}
		return searchedList;
		
	}
}
