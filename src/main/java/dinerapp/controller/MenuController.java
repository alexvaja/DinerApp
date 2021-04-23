package dinerapp.controller;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.annotation.SessionScope;

import dinerapp.constants.MenuStates;
import dinerapp.exceptions.DuplicateCategoryException;
import dinerapp.exceptions.NewSessionException;
import dinerapp.exceptions.WrongMenuDateException;
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
import dinerapp.security.utils.CategoryComparer;
import dinerapp.security.utils.FoodComparer;
import dinerapp.validator.MenuValidator;

@Controller
@Component
public class MenuController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private FoodRepository foodRepository;

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private DishRepository dishRepository;

	@Autowired
	private MenuValidator validator;

	@ExceptionHandler({ NewSessionException.class })
	public String sessionError() {
		LOGGER.error("NEW SESSION ERROR");
		return "views/loginView";
	}

	@SessionScope
	@GetMapping("/menuView")
	public String getController(HttpSession session, Model model, Principal principal)
			throws NewSessionException, WrongMenuDateException {

		LOGGER.info("-----------------------------------------------");
		LOGGER.info("-- MenuController -> GET METHOD --");
		LOGGER.info("Session ViewModel attribute: " + session.getAttribute("menuViewModel"));

		if (session.isNew()) {
			throw new NewSessionException();
		}

		session.setAttribute("menuViewModel", new MenuViewModel());
		model.addAttribute("addMenuIsAvailable", false);

		return "views/menuView";
	}

	@Transactional
	@PostMapping("/menuView")
	public String postController(Model model, HttpSession session, @SessionAttribute MenuViewModel menuViewModel,
			@RequestParam(value = "submit") String reqParam,
			@RequestParam(value = "menu_title", required = false) String menuTitle,
			@RequestParam(value = "menu_date", required = false) String menuDate,
			@RequestParam(value = "dropdown_list", required = false) String selectedMenuCategories,
			@RequestParam(value = "checkbox_list", required = false) String selectedMenuFoods)
			throws DuplicateCategoryException, ParseException, WrongMenuDateException {

		LOGGER.info("-----------------------------------------------");
		LOGGER.info("-- MenuController -> POST METHOD --");
		LOGGER.info("Session ViewModel attribute: " + session.getAttribute("menuViewModel"));
		LOGGER.info("Request param from the form: " + reqParam);
		LOGGER.info("Menu title from the form: " + menuTitle);
		LOGGER.info("Menu date from the form:" + menuDate);
		LOGGER.info("Selected Categories from the form: " + selectedMenuCategories);
		LOGGER.info("Selected foods from the form: " + selectedMenuFoods);

		model.addAttribute("addMenuIsAvailable", true);

		switch (reqParam) {
		case "Adauga Meniu": {

			LOGGER.info("-- 'Adauga Meniu' CASE --");

			MenuDTO menuDTO = new MenuDTO();
			menuDTO.setState(menuViewModel.getMenuDTO().getState());

			List<DishDTO> dishes = menuViewModel.getDishesDTO();

			dishes.add(createDefaultDishesDTO());

			menuViewModel.setMenuDTO(menuDTO);
			menuViewModel.setDishesDTO(dishes);

			break;
		}
		case "Adauga Categorie Noua": {

			LOGGER.info("-- 'Adauga Categorie Noua' CASE --");

			MenuDTO menuDTO = new MenuDTO();
			menuDTO.setId(menuViewModel.getMenuDTO().getId());
			menuDTO.setTitle(menuTitle);
			menuDTO.setDate(menuDate);
			menuDTO.setState(menuViewModel.getMenuDTO().getState());

			List<DishDTO> dishes = menuViewModel.getDishesDTO();

			if (selectedMenuCategories != null) {
				updateListSelectedCategory(selectedMenuCategories, dishes);
			}

			if (selectedMenuFoods != null) {
				updateListSelectedFoods(selectedMenuFoods, dishes);
			}

			dishes.add(createDefaultDishesDTO());

			menuViewModel.setMenuDTO(menuDTO);

			break;

		}
		case "Anuleaza": {

			LOGGER.info("-- 'Anuleaza' CASE --");

			session.removeAttribute("menuViewModel");
			model.addAttribute("addMenuIsAvailable", false);

			return "redirect:/viewMenuView";
		}
		case "Salvare": {

			LOGGER.info("-- 'Salvare' CASE --");

			MenuDTO menuDTO = new MenuDTO();
			menuDTO.setId(menuViewModel.getMenuDTO().getId());
			menuDTO.setTitle(menuTitle);
			menuDTO.setDate(menuDate);
			menuDTO.setState(menuViewModel.getMenuDTO().getState());
			menuViewModel.setMenuDTO(menuDTO);

			List<DishDTO> dishes = menuViewModel.getDishesDTO();

			if (selectedMenuCategories != null) {
				updateListSelectedCategory(selectedMenuCategories, dishes);
			}

			if (selectedMenuFoods != null) {
				updateListSelectedFoods(selectedMenuFoods, dishes);
			}

			String errorMessage = validator.dateValidator(menuViewModel);
			LOGGER.info("MARE MARE MESSAGE: " + errorMessage);

			if (errorMessage == null) {

				if (!areNotDuplicateCategories(dishes)) {
					session.setAttribute("menuViewModel", menuViewModel);
					model.addAttribute("categoryError", "Categoriile trebuie sa fie diferite!");
					return "views/menuView";
				}

				boolean x = fffff(dishes);
				System.err.println("FUNCTIE: " + x);

				if (!x) {
					session.setAttribute("menuViewModel", menuViewModel);
					model.addAttribute("dateError", "Eroare de server!!!");
					return "views/menuView";
				}

				List<Dish> selectedDishList = new ArrayList<>();

				Menu menu = new Menu();
				menu.setId(menuViewModel.getMenuDTO().getId());
				menu.setDate(menuViewModel.getMenuDTO().getDate());
				menu.setTitle(menuViewModel.getMenuDTO().getTitle());
				menu.setState(MenuStates.SAVED.toString());

				menuRepository.save(menu);

				for (DishDTO dishDTO : dishes) {
					List<Food> selectedFoods = getSelectedFoodsForCategory(dishDTO.getFoods());

					if (!selectedFoods.isEmpty()) {
						if (dishDTO.getId() == null) {
							Dish dish = new Dish();
							dish.setCategory(getSelectedCategory(dishDTO.getCategories()));
							dish.setFoods(selectedFoods);
							dish.setMenu(menu);
							dishRepository.save(dish);
							selectedDishList.add(dish);
						} else {
							Optional<Dish> d = dishRepository.findById(dishDTO.getId());
							Dish dish = d.get();
							dish.setCategory(getSelectedCategory(dishDTO.getCategories()));
							dish.setFoods(selectedFoods);
							dish.setMenu(menu);
							dishRepository.save(dish);
							selectedDishList.add(dish);
						}
					} else if (dishDTO.getId() != null) {
						dishRepository.deleteById(dishDTO.getId());
					}
				}

				session.removeAttribute("menuViewModel");
			} else {
				session.setAttribute("menuViewModel", menuViewModel);
				model.addAttribute("dateError", errorMessage);
				return "views/menuView";
			}

			return "redirect:/viewMenuView";
		}
		}

		return "views/menuView";
	}

	private boolean fffff(List<DishDTO> dishes) {

		List<Category> categories = getAllCategoriesFromTable();
		List<Food> foods = getAllFoodsFromTable();

		for (DishDTO dishDTO : dishes) {

			List<Food> foodForSave = getSelectedFoodsForCategory(dishDTO.getFoods());

			for (Food food : foodForSave) {

				if (!foods.contains(food)) {
					return false;
				}
			}

		}

		List<Category> cat = getAllCategoriesFromMenu(dishes);

		for (Category c : cat) {

			if (!categories.contains(c)) {
				return false;
			}
		}

		return true;
	}

	private List<Category> getAllCategoriesFromMenu(List<DishDTO> dishesDTO) {

		List<Category> categoriesFromMenu = new ArrayList<>();

		for (DishDTO dishDTO : dishesDTO) {
			for (CategoryDTO categoryDTO : dishDTO.getCategories()) {
				if (categoryDTO.getSelected()) {
					categoriesFromMenu.add(categoryDTO.getCategory());
				}
			}
		}

		return categoriesFromMenu;
	}

	private Category getSelectedCategory(List<CategoryDTO> savedCategory) {

		for (CategoryDTO categoryDTO : savedCategory) {
			if (categoryDTO.getSelected()) {
				return categoryDTO.getCategory();
			}
		}
		return null;
	}

	private List<Food> getSelectedFoodsForCategory(List<FoodDTO> savedFoods) {

		List<Food> selectedFoods = new ArrayList<>();

		for (FoodDTO foodDTO : savedFoods) {
			if (foodDTO.getSelected()) {
				selectedFoods.add(foodDTO.getFood());
			}
		}
		return selectedFoods;
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
			String lowerCategoryName = (category.getName().toLowerCase());
			String categoryName = lowerCategoryName.substring(0, 1).toUpperCase() + lowerCategoryName.substring(1);
			category.setName(categoryName);
			searchedList.add(category);
		}
		sortCategoriesByName(searchedList);
		return searchedList;
	}

	private List<Food> getAllFoodsFromTable() {

		Iterable<Food> list = foodRepository.findAll();
		List<Food> searchedList = new ArrayList<>();

		for (Food food : list) {

			String lowerFoodName = (food.getName().toLowerCase());
			String foodName = lowerFoodName.substring(0, 1).toUpperCase() + lowerFoodName.substring(1);

			String lowerFoodIngr = (food.getIngredients().toLowerCase());
			String foodIngr = lowerFoodIngr.substring(0, 1).toUpperCase() + lowerFoodIngr.substring(1);

			food.setName(foodName);
			food.setIngredients(foodIngr);

			searchedList.add(food);
		}
		sortFoodsByName(searchedList);
		return searchedList;
	}

	private void updateListSelectedCategory(String selectedMenuCategories, List<DishDTO> dishes) {

		int index = 0;
		String[] indexCategory = selectedMenuCategories.split(",");

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

		int index = 0;
		String[] indexFood = selectedMenuFoods.split(",");

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

	private boolean areNotDuplicateCategories(List<DishDTO> dishes) {

		List<Category> categories = getAllCategoriesFromMenu(dishes);
		List<Food> foodsForFirstCategory = new ArrayList<>();
		List<Food> foodsForSecondCategory = new ArrayList<>();

		for (int i = 0; i < categories.size() - 1; i++) {
			for (int j = i + 1; j < categories.size(); j++) {

				foodsForFirstCategory = getSelectedFoodsForCategory(dishes.get(i).getFoods());
				foodsForSecondCategory = getSelectedFoodsForCategory(dishes.get(j).getFoods());
				if (categories.get(i).getName().equals(categories.get(j).getName())) {
					if (foodsForFirstCategory.isEmpty() || foodsForSecondCategory.isEmpty()) {
						continue;
					}
					return false;
				}
			}
		}
		return true;
	}

	private void sortCategoriesByName(List<Category> categories) {
		Collections.sort(categories, new CategoryComparer());
	}

	private void sortFoodsByName(List<Food> foods) {
		Collections.sort(foods, new FoodComparer());
	}
}
