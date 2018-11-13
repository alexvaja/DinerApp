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
	
	@PostMapping("/menuView")
	public String setAllMenu(Model model, @RequestParam("submit") String reqParam) {
		System.out.println(reqParam);
		
		MenuViewModel menuViewModel = new MenuViewModel();
		menuViewModel.setCategoryItems(getListOfCategory());
		menuViewModel.setFoodItems(getlistOfFood());
		menuViewModel.setMenuItems(null);
		Boolean addMenuIsAvailable = false;
		Boolean addCategoryIsAvailable = false;
		Boolean addFoodIsAvailable = false;
		model.addAttribute("menuViewModel", menuViewModel);
		model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
		model.addAttribute("addFoodIsAvailable", addFoodIsAvailable);
		model.addAttribute("addFoodIsAvailable", addFoodIsAvailable);
		
		switch(reqParam) {
		case "AddMenu":
			System.out.println("Am intart in AddMenu");
			
			addMenuIsAvailable = true;
			model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
			break;
		case "AddCategory":
			System.out.println("Am intrat in AddCategory");
			
			addMenuIsAvailable = true;
			model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
			addCategoryIsAvailable = true;
			model.addAttribute("addCategoryIsAvailable", addCategoryIsAvailable);
			
//			List<Category> categoryList = new ArrayList<>(getListOfCategory());
//			model.addAttribute("categoryList", categoryList);
			model.addAttribute("menuViewModel", menuViewModel);
			
			break;
		case "AddFood":
			System.out.println("Am intrat in AddFood");
			
			addMenuIsAvailable = true;
			addCategoryIsAvailable = true;
			addFoodIsAvailable = true;
			model.addAttribute("menuViewModel", menuViewModel);
			model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
			model.addAttribute("addCategoryIsAvailable", addCategoryIsAvailable);
			model.addAttribute("addFoodIsAvailable", addFoodIsAvailable);
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
