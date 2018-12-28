package dinerapp.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.swing.plaf.MenuItemUI;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.annotation.SessionScope;

import dinerapp.constants.MenuStates;
import dinerapp.model.MenuViewModel;
import dinerapp.model.dto.CategoryDTO;
import dinerapp.model.dto.DishDTO;
import dinerapp.model.dto.FoodDTO;
import dinerapp.model.entity.Category;
import dinerapp.model.entity.Dish;
import dinerapp.model.entity.Food;
import dinerapp.model.entity.Menu;
import dinerapp.repository.CategoryRepository;
import dinerapp.repository.DishRepository;
import dinerapp.repository.FoodRepository;
import dinerapp.repository.MenuRepository;

@Controller
public class MenuController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	FoodRepository foodRepository;

	@Autowired
	MenuRepository menuRepository;

	@Autowired
	DishRepository dishRepository;

	@SessionScope
	@GetMapping("/menuView")
	public String sessionExample(Model model, Principal principal, HttpSession session) {
		LOGGER.info("GET MENU");
		session.setAttribute("menuViewModel", new MenuViewModel());

		// vaja style
		Boolean addMenuIsAvailable = false;
		model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);

		return "views/menuView";
	}

	@Transactional
	@PostMapping("/menuView")
	public String setAllMenu(Model model, HttpSession session, @SessionAttribute MenuViewModel menuViewModel,
			@RequestParam(value = "submit") String reqParam,
			@RequestParam(value = "menu_title", required = false) String menuTitle,
			@RequestParam(value = "menu_date", required = false) String menuDate,
			@RequestParam(value = "dropdown_list", required = false) String selectedMenuCategories,
			@RequestParam(value = "checkbox_list", required = false) String selectedMenuFoods) {

		LOGGER.info("SET MENU");
		Boolean addMenuIsAvailable = false;
		model.addAttribute("add MenuIsAvailable", addMenuIsAvailable);

		switch (reqParam) {
		case "AddMenu": {
			LOGGER.info("Am intrat in AddMenu case");
			List<DishDTO> dishes = menuViewModel.getDishes();

			addMenuIsAvailable = true;
			model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);

			if (selectedMenuCategories != null) {
				updateListSelectedCategory(selectedMenuCategories, dishes);
			}

			if (selectedMenuFoods != null) {
				updateListSelectedFoods(selectedMenuFoods, dishes);
			}

			dishes.add(createDefaultDishesDTO());

			menuViewModel.setDishes(dishes);
			menuViewModel.setTitle(menuTitle);
			menuViewModel.setDate(menuDate);

			break;
		}
		case "Cancel": {
			addMenuIsAvailable = false;
			session.removeAttribute("menuViewModel");
			session.setAttribute("menuViewModel", new MenuViewModel());
			break;
		}
		case "SaveAll": {
			List<DishDTO> dishes = menuViewModel.getDishes();
			
			if (menuDate == null || !isDateExist(menuDate)) {
				List<Dish> selectedDishList = new ArrayList<>();

				Menu menu = new Menu();

				menu.setData(menuDate);
				menu.setTitle(menuTitle);
				menu.setState(MenuStates.SAVED.toString());
				menuRepository.save(menu);

				if (selectedMenuCategories != null) {
					updateListSelectedCategory(selectedMenuCategories, dishes);
				}

				if (selectedMenuFoods != null) {
					updateListSelectedFoods(selectedMenuFoods, dishes);
				}

				for (DishDTO dishDTO : dishes) {
					Dish dish = new Dish();

					dish.setCategory(getSelectedCategory(dishDTO.getCategories()));

					List<FoodDTO> savedFood = dishDTO.getFoods();
					List<Food> selectedFood = new ArrayList<>();

					for (FoodDTO foodDTO : savedFood) {
						if (foodDTO.getSelected() == true) {
							selectedFood.add(foodDTO.getFood());
						}
					}

					if (!selectedFood.isEmpty()) {
						dish.setFoods(selectedFood);
						dish.setMenu(menu);
						dishRepository.save(dish);
						selectedDishList.add(dish);
					}
				}
				// menu.setDishes(selectedDishList);

				addMenuIsAvailable = false;
				session.removeAttribute("menuViewModel");
				session.setAttribute("menuViewModel", new MenuViewModel());
			}

			break;
		}
		case "Edit": {
			List<DishDTO> dishes = new ArrayList<>();
			addMenuIsAvailable = true;

			List<Menu> listOfMenus = getAllMenusFromTable();

			Menu menu = listOfMenus.get(listOfMenus.size() - 1);

			System.out.println("Meniu: " + menu);

			for (Dish dish : menu.getDishes()) {
				System.out.println("DISH: " + dish);

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

				dishDTO.setCategories(categoriesDTO);
				dishDTO.setFoods(foodsDTO);
				System.out.println(dishDTO);
				dishes.add(dishDTO);
			}

			System.out.println("Titlul: " + menuTitle);
			System.out.println("Date: " + menuDate);
			System.out.println("List categories: " + selectedMenuCategories);
			System.out.println("List foods: " + selectedMenuFoods);

			// category
			if (selectedMenuCategories != null) {

				String[] indexCategory = null;

				indexCategory = selectedMenuCategories.split(",");

				int index = 0;

				for (DishDTO dishDTO : dishes) {
					List<CategoryDTO> listCategory = dishDTO.getCategories();

					for (CategoryDTO categoryDTO : listCategory) {
						categoryDTO.setSelected(false);
					}

					listCategory.get(Integer.parseInt(indexCategory[index])).setSelected(true);

					index++;
					dishDTO.setCategories(listCategory);
				}
			}

			// food
			if (selectedMenuFoods != null) {

				String[] indexFood = selectedMenuFoods.split(",");

				int index = 0;

				for (DishDTO dishDTO : dishes) {
					List<FoodDTO> listFood = dishDTO.getFoods();

					for (FoodDTO foodDTO : listFood) {
						foodDTO.setSelected(false);
					}

					while (Integer.valueOf(indexFood[index]) != -1) {
						listFood.get(Integer.valueOf(indexFood[index])).setSelected(true);
						index++;
					}

					index++;
					dishDTO.setFoods(listFood);
				}
			}

			menuViewModel.setDate(menu.getData());
			menuViewModel.setTitle(menu.getTitle());
			menuViewModel.setDishes(dishes);
			menuViewModel.setState(menu.getState());

			session.setAttribute("menuViewModel", menuViewModel);
			break;
		}
		}

		model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
		return "views/menuView";
	}
	
	private Category getSelectedCategory(List<CategoryDTO> savedCategory) {
		for (CategoryDTO categoryDTO : savedCategory) {
			if (categoryDTO.getSelected() == true) {
				return categoryDTO.getCategory();
			}
		}
		return null;
	}
	
	private List<Food> getSelectedFoodsForCategory(List<FoodDTO> savedFoods) {
		List<Food> selectedFoods = new ArrayList<>();

		for (FoodDTO foodDTO : savedFoods) {
			if (foodDTO.getSelected() == true) {
				selectedFoods.add(foodDTO.getFood());
			}
		}
		return selectedFoods;
	}
	
	private Boolean isDateExist(String menuDate) {
		List<Menu> menuList = getAllMenusFromTable();
		
		for (Menu menu : menuList) {
			if (menu.getData().equals(menuDate)) {
				return true;
			}
		}
		return false;
	}

	private DishDTO createDefaultDishesDTO() {
		List<Category> listOfCategories = getAllCategoriesFromTable();
		List<Food> listOfFoods = getAllFoodsFromTable();

		List<CategoryDTO> listOfCategoriesDTO = createAllCategoriesDTO(listOfCategories);
		List<FoodDTO> listOfFoodsDTO = createAllFoodsDTO(listOfFoods);

		DishDTO dishDTO = new DishDTO();
		dishDTO.setCategories(listOfCategoriesDTO);
		dishDTO.setFoods(listOfFoodsDTO);

		return dishDTO;
	}

	private List<CategoryDTO> createAllCategoriesDTO(List<Category> listOfCategories) {
		List<CategoryDTO> listOfCategoriesDTO = new ArrayList<>();

		for (Category category : listOfCategories) {
			listOfCategoriesDTO.add(new CategoryDTO(category, false));
		}

		return listOfCategoriesDTO;
	}

	private List<FoodDTO> createAllFoodsDTO(List<Food> listOfFoods) {
		List<FoodDTO> listOfFoodsDTO = new ArrayList<>();

		for (Food food : listOfFoods) {
			listOfFoodsDTO.add(new FoodDTO(food, false));
		}

		return listOfFoodsDTO;
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

	private void updateListSelectedCategory(String selectedMenuCategories, List<DishDTO> dishes) {
		String[] indexCategory = selectedMenuCategories.split(",");
		int index = 0;

		for (DishDTO dishDTO : dishes) {
			List<CategoryDTO> listCategory = dishDTO.getCategories();

			for (CategoryDTO categoryDTO : listCategory) {
				categoryDTO.setSelected(false);
			}

			listCategory.get(Integer.parseInt(indexCategory[index])).setSelected(true);
			index++;

			dishDTO.setCategories(listCategory);
		}
	}

	private void updateListSelectedFoods(String selectedMenuFoods, List<DishDTO> dishes) {
		String[] indexFood = selectedMenuFoods.split(",");
		int index = 0;

		for (DishDTO dishDTO : dishes) {
			List<FoodDTO> listFood = dishDTO.getFoods();

			for (FoodDTO foodDTO : listFood) {
				foodDTO.setSelected(false);
			}

			while (Integer.valueOf(indexFood[index]) != -1) {
				listFood.get(Integer.valueOf(indexFood[index])).setSelected(true);
				index++;
			}

			index++;
			dishDTO.setFoods(listFood);
		}
	}
}