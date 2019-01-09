package dinerapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dinerapp.constants.MenuStates;
import dinerapp.model.MenuViewModel;
import dinerapp.model.dto.CategoryDTO;
import dinerapp.model.dto.DishDTO;
import dinerapp.model.dto.FoodDTO;
import dinerapp.model.dto.MenuDTO;
import dinerapp.model.entity.Category;
import dinerapp.model.entity.Dish;
import dinerapp.model.entity.Food;
import dinerapp.model.entity.Menu;
import dinerapp.repository.CategoryRepository;
import dinerapp.repository.DishRepository;
import dinerapp.repository.FoodRepository;
import dinerapp.repository.MenuRepository;

@Controller
public class ViewMenuController {

	//private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	FoodRepository foodRepository;
	@Autowired
	MenuRepository menuRepository;

	@Autowired
	DishRepository dishRepository;

	@GetMapping("/viewMenuView")
	public String getAllMenus() {
		return "views/viewMenuView";
	}

	@PostMapping("/viewMenuView")
	public String setAllMenus(HttpSession session, @RequestParam("submit") String reqParam, Model model) {

		switch (reqParam) {
		case "Edit": {
			LOGGER.info("ViewMenuController - Edit case");
			
			List<DishDTO> dishes = new ArrayList<>();
			List<Menu> listOfMenus = getAllMenusFromTable();

			// de inlocuit
			Menu menu = listOfMenus.get(listOfMenus.size() - 1);
			
			for (Dish dish : menu.getDishes()) {
				DishDTO dishDTO = new DishDTO();
				List<CategoryDTO> categoriesDTO = new ArrayList<>();

				for (Category category : getAllCategoriesFromTable()) {
					if (category.equals(dish.getCategory())) {
						categoriesDTO.add(new CategoryDTO(category, true));
					} else {
						categoriesDTO.add(new CategoryDTO(category, false));
					}
				}

				List<FoodDTO> foodsDTO = new ArrayList<>();
				List<Food> lastSavedFoods = dish.getFoods();
				for (Food food : getAllFoodsFromTable()) {
					foodsDTO.add(new FoodDTO(food, false));
				}

				for (FoodDTO foodDTO : foodsDTO) {
					for (Food food : lastSavedFoods) {
						if (foodDTO.getFood().equals(food)) {
							foodDTO.setSelected(true);
						}
					}
				}
				dishDTO.setId(dish.getId());
				dishDTO.setCategories(categoriesDTO);
				dishDTO.setFoods(foodsDTO);
				dishes.add(dishDTO);
			}
			
			MenuViewModel menuViewModel = new MenuViewModel();

			MenuDTO menuDTO = new MenuDTO();
			menuDTO.setId(menu.getId());
			menuDTO.setDate(menu.getData());
			menuDTO.setState(menu.getState());
			System.out.println("Menu State in db: " + menuDTO.getState());
			menuDTO.setTitle(menu.getTitle());
			menuViewModel.setMenu(menuDTO);
			
			
			//menuViewModel.setDate(menu.getData());
			//menuViewModel.setTitle(menu.getTitle());
			menuViewModel.setDishes(dishes);
			//menuViewModel.setState(menu.getState());

			session.setAttribute("menuViewModel", menuViewModel);
			model.addAttribute("addMenuIsAvailable", true);
			return "views/menuView";
		}
		case "Publish":
			List<Menu> listOfMenus = getAllMenusFromTable();
			Menu menu = listOfMenus.get(listOfMenus.size() - 1);
			menu.setState(MenuStates.PUBLISHED.toString());
			menuRepository.save(menu);
			break;
		}

		return "views/viewMenuView";
	}

	private List<Category> getAllCategoriesFromTable() {

		Iterable<Category> list = categoryRepository.findAll();
		List<Category> searchedList = new ArrayList<>();
		for (Category category : list) {
			searchedList.add(category);
		}
		return searchedList;
	}

	public List<Menu> getAllMenusFromTable() {
		
		Iterable<Menu> list = menuRepository.findAll();
		List<Menu> searchedList = new ArrayList<>();
		for (Menu menu : list) {
			searchedList.add(menu);
		}
		return searchedList;
	}

	private List<Food> getAllFoodsFromTable() {

		Iterable<Food> list = foodRepository.findAll();
		List<Food> searchedList = new ArrayList<>();
		for (Food food : list) {
			searchedList.add(food);
		}
		return searchedList;
	}
}