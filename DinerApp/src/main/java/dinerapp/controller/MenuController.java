package dinerapp.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.annotation.SessionScope;

import dinerapp.dto.CategoryDTO;
import dinerapp.dto.DishDTO;
import dinerapp.dto.FoodDTO;
import dinerapp.model.MenuViewModel;
import dinerapp.model.entity.Category;
import dinerapp.model.entity.Food;
import dinerapp.repository.CategoryRepository;
import dinerapp.repository.FoodRepository;

@Controller
public class MenuController {
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	FoodRepository foodRepository;
	
	@SessionScope
	@GetMapping("/menuView")
	public String sessionExample(Model model, Principal principal, HttpSession session) {
			session.setAttribute("menuViewModel", new MenuViewModel());
			
			
			Boolean addMenuIsAvailable = false;
			model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
			
			return "menuView";
	} 
	
	@PostMapping("/menuView")
	public String setAllMenu(Model model, @SessionAttribute MenuViewModel menuViewModel,
										  @RequestParam("submit") String reqParam) {
		
		System.out.println("Vine param: " + reqParam);
		
		Boolean addMenuIsAvailable = false;
		model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);

		List<Category> categoryList = getListOfCategory();
		List<Food> foodList = getlistOfFood();

		List<DishDTO> dishes = menuViewModel.getDishes();
		String title = menuViewModel.getTitle();
		String date = menuViewModel.getDate();
		
		//String[] index = reqParam.split(",");
		
		switch(reqParam) {
		case "AddMenu":
			
			addMenuIsAvailable = true;
			model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
			
			List<CategoryDTO> categories = new ArrayList<>();
			List<FoodDTO> foods = new ArrayList<>();

			
			for (Category category : categoryList) {
				categories.add(new CategoryDTO(category, false));
			}
			
			for (Food food : foodList) {
				foods.add(new FoodDTO(food, false));
			}
			
			DishDTO dish = new DishDTO();
			dish.setCategories(categories);
			dish.setFoods(foods);
			
			dishes.add(dish);
			menuViewModel.setDishes(dishes);
			menuViewModel.setDate(date);
			menuViewModel.setTitle(title);
			break;
		case "Cancel":
			break;
		case "SaveAll":
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
	
//	private MenuViewModel getVM() 
//	{
//		MenuViewModel menuViewModel = new MenuViewModel();
//		//Category
//		Category category1 = new Category(1, "Meniu 1", 17.);
//		
//		Category category2 = new Category(2, "Meniu 2", 19.);
//		
//		Category category3 = new Category(3, "Meniu Vegetarian", 20.5);
//		//Food
//		Food food1 = new Food(5, "Ciorba", "apa, sare, piper, morcovi", 250, 6);
//
//		Food food2 = new Food(6, "Supa", "apa, sare, piper, morcovi", 300, 5);
//		
//		Food food3 = new Food(7, "Tocanita", "apa, sare, piper, morcovi", 260, 10);
//		
//		Food food4 = new Food(8, "Gulas", "apa, sare, piper, morcovi", 600, 12);
//		//Dish
//		Dish dish1 = new Dish();
//		dish1.setCategory(category1);
//		dish1.addNewFood(food1);
//		dish1.addNewFood(food3);
//		dish1.addNewFood(food4);
//		
//		Dish dish2 = new Dish();
//		dish2.setCategory(category2);
//		dish2.addNewFood(food1);
//		dish2.addNewFood(food3);
//		dish2.addNewFood(food4);
//		
//		Dish dish3 = new Dish();
//		dish3.setCategory(category3);
//		dish3.addNewFood(food1);
//		dish3.addNewFood(food2);
//		dish3.addNewFood(food3);
//		dish3.addNewFood(food4);
//		
//		menuViewModel.addNewDish(dish1);
//		menuViewModel.addNewDish(dish2);
//		menuViewModel.addNewDish(dish3);
//		menuViewModel.addNewDish(dish1);
//		menuViewModel.addNewDish(dish2);
//		
//		return menuViewModel;
//	}
	
	
//	List<DishDTO> dishes1 = menuViewModel.getDishes();
//	
//	List<FoodDTO> f = new ArrayList<>();
//	List<CategoryDTO> c = new ArrayList<>();
//	f.add(new FoodDTO(new Food(5, "Ciorba", "apa, sare, piper, morcovi", 250, 6), false));
//	f.add(new FoodDTO(new Food(5, "Ciorba", "apa, sare, piper, morcovi", 250, 6), true));
//	f.add(new FoodDTO(new Food(5, "Ciorba", "apa, sare, piper, morcovi", 250, 6), false));
//	f.add(new FoodDTO(new Food(5, "Ciorba", "apa, sare, piper, morcovi", 250, 6), false));
//	f.add(new FoodDTO(new Food(5, "Ciorba", "apa, sare, piper, morcovi", 250, 6), true));
//	f.add(new FoodDTO(new Food(5, "Ciorba", "apa, sare, piper, morcovi", 250, 6), true));
//	
//	c.add(new CategoryDTO(new Category(2, "Meniu 2", 19.), true));
//	dishes1.add(new DishDTO(c, f));
//
//	menuViewModel.setDishes(dishes1);
//	menuViewModel.setDate(date);
//	menuViewModel.setTitle(title);
	
	
	
	
}
