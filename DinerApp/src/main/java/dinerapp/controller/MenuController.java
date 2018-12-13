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
import dinerapp.entity.Dish;
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
	
	List<Integer> numberOfMenu = new ArrayList<>();
//
	@GetMapping("/menuView")
	public String getAllMenu(Model model) {
		
		Boolean addMenuIsAvailable = false;
		numberOfMenu.removeAll(numberOfMenu);
		model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
		model.addAttribute("numberOfMenu", numberOfMenu);
		MenuViewModel menuViewModel = new MenuViewModel();
		model.addAttribute("menuViewModel", menuViewModel);
		
		return "menuView";
	}
	
	@PostMapping("/menuView")
	public String setAllMenu(Model model, @RequestParam("submit") String reqParam, 
										  @ModelAttribute("menuViewModel") MenuViewModel menuViewModel,
										  @ModelAttribute("dish") Dish dish) {
		System.out.println(dish);
		System.out.println(menuViewModel);
		System.out.println(numberOfMenu.size());
		System.out.println("");
		Boolean addMenuIsAvailable = false;
		model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
		model.addAttribute("categoryList", getListOfCategory());
		model.addAttribute("foodList", getlistOfFood());
		model.addAttribute("numberOfMenu", numberOfMenu);
		
		switch(reqParam) {
		case "AddMenu":
			
			addMenuIsAvailable = true;
			model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
			numberOfMenu.add(null);
			model.addAttribute("numberOfMenu", numberOfMenu);
			break;
		case "Cancel":
			numberOfMenu.removeAll(numberOfMenu);
			model.addAttribute("numberOfMenu", numberOfMenu);
			break;
		case "SaveAll":
			numberOfMenu.removeAll(numberOfMenu);
			model.addAttribute("numberOfMenu", numberOfMenu);
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
	
	private List<Food> getlistOfFood() {
		
		Iterable<Food> list = foodRepository.findAll();
		List<Food> searchedList = new ArrayList<>();
		for (Food food : list) {
			searchedList.add(food);
		}
		return searchedList;	
	}
}