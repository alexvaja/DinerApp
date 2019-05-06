package dinerapp.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import dinerapp.exceptions.WrongDataFormatException;
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

	@ExceptionHandler({ NewSessionException.class })
	public String sessionError() {
		LOGGER.error("NEW SESSION ERROR");
		return "views/loginView";
	}

	@ExceptionHandler({ WrongDataFormatException.class })
	public String dateFormatError() {
		LOGGER.error("WRONG FORMAT ERROR");
		return "redirect:menuView";
	}

	@ExceptionHandler({ WrongMenuDateException.class })
	public String dateError() {
		LOGGER.error("WRONG DATE ERROR");
		return "redirect:menuView";
	}

	@ExceptionHandler({ DuplicateCategoryException.class })
	public String duplicateError() {
		LOGGER.error("DUPLICATE CATEGORIES ERROR");
		return "redirect:menuView";
	}

	@SessionScope
	@GetMapping("/menuView")
	public String getMethod(Model model, Principal principal, HttpSession session)
			throws NewSessionException, WrongMenuDateException {

		LOGGER.info("CLASS NAME: " + MenuController.class.getName());
		LOGGER.info("-- GET METHOD --");
		LOGGER.info("Session ViewModel attribute: " + session.getAttribute("menuViewModel"));

		if (session.isNew()) {
			throw new NewSessionException();
		}

		if (session.getAttribute("menuViewModel") == null) {

			session.removeAttribute("menuViewModel");
			session.setAttribute("menuViewModel", new MenuViewModel());

			session.setAttribute("formatDateError", false);
			session.setAttribute("dateErrorMessage", false);
			session.setAttribute("dublicateCategoriesError", false);

			model.addAttribute("addMenuIsAvailable", false);
		} else {

			LOGGER.info("Date format error message: " + session.getAttribute("formatDateError"));
			LOGGER.info("Date error message: " + session.getAttribute("dateErrorMessage"));
			LOGGER.info("Category error message: " + session.getAttribute("dublicateCategoriesError"));

			if ((boolean) session.getAttribute("formatDateError")) {
				session.setAttribute("dateErrorMessage", false);
				session.setAttribute("dublicateCategoriesError", false);
			}

			if ((boolean) session.getAttribute("dateErrorMessage")) {
				session.setAttribute("formatDateError", false);
				session.setAttribute("dublicateCategoriesError", false);
			}

			if ((boolean) session.getAttribute("dublicateCategoriesError")) {
				session.setAttribute("formatDateError", false);
				session.setAttribute("dateErrorMessage", false);
			}

			model.addAttribute("addMenuIsAvailable", true);
		}
		return "views/menuView";
	}

	@Transactional
	@PostMapping("/menuView")
	public String postMethod(Model model, HttpSession session, @SessionAttribute MenuViewModel menuViewModel,
			@RequestParam(value = "submit") String reqParam,
			@RequestParam(value = "menu_title", required = false) String menuTitle,
			@RequestParam(value = "menu_date", required = false) String menuDate,
			@RequestParam(value = "dropdown_list", required = false) String selectedMenuCategories,
			@RequestParam(value = "checkbox_list", required = false) String selectedMenuFoods)
			throws DuplicateCategoryException, ParseException, WrongMenuDateException {

		LOGGER.info("CLASS NAME: " + MenuController.class.getName());
		LOGGER.info("-- POST METHOD --");
		LOGGER.info("Session ViewModel attribute: " + session.getAttribute("menuViewModel"));
		LOGGER.info("Request param from the form: " + reqParam);
		LOGGER.info("Menu title from the form: " + menuTitle);
		LOGGER.info("Menu date from the form:" + menuDate);
		LOGGER.info("Selected Categories from the form: " + selectedMenuCategories);
		LOGGER.info("Selected foods from the form: " + selectedMenuFoods);

		session.setAttribute("formatDateError", false);
		session.setAttribute("dateErrorMessage", false);
		session.setAttribute("dublicateCategoriesError", false);

		switch (reqParam) {
		case "Adauga Meniu": {

			LOGGER.info("-- 'Adauga Meniu' CASE --");

			MenuDTO menuDTO = new MenuDTO();
			menuDTO.setState(menuViewModel.getMenuDTO().getState());

			List<DishDTO> dishes = menuViewModel.getDishesDTO();

			dishes.add(createDefaultDishesDTO());

			menuViewModel.setMenuDTO(menuDTO);
			menuViewModel.setDishesDTO(dishes);

			model.addAttribute("addMenuIsAvailable", true);

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
			menuViewModel.setDishesDTO(dishes);

			model.addAttribute("addMenuIsAvailable", true);

			break;

		}
		case "Anuleaza": {

			LOGGER.info("-- 'Anuleaza' CASE --");

			session.setAttribute("formatDateError", false);
			session.setAttribute("dateErrorMessage", false);
			session.setAttribute("dublicateCategoriesError", false);

			session.removeAttribute("menuViewModel");
			session.setAttribute("menuViewModel", new MenuViewModel());

			model.addAttribute("addMenuIsAvailable", false);

			break;
		}
		case "Salvare": {

			LOGGER.info("-- 'Salvare' CASE --");

			String date = menuViewModel.getMenuDTO().getDate();

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

			if (menuDate.isEmpty()) {
				session.setAttribute("formatDateError", true);
				throw new WrongDataFormatException("Date does not exist!");
			}

			if (!isDateInRightFormat(menuDate)) {
				session.setAttribute("formatDateError", true);
				throw new WrongDataFormatException("Date does not have the right format!");
			}

			if (!isDateGreaterThanToday(menuDate)) {
				throw new WrongMenuDateException("Date must be greater than today!");
			}

			if (isDateThatNotExistInDB(menuDate, menuViewModel.getMenuDTO().getState(), date)) {

				if (!areNotDuplicateCategories(dishes)) {
					session.setAttribute("dublicateCategoriesError", true);
					throw new DuplicateCategoryException("Categorii dublicate!");
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
				session.setAttribute("dateErrorMessage", true);
				throw new WrongMenuDateException("There is a menu already saved on this date!");
			}

			return "redirect:/viewMenuView";
		}
		}

		return "views/menuView";
	}

	private boolean isDateInRightFormat(String date) {

		String datePattern = "20\\d\\d-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])$";

		Pattern pattern = Pattern.compile(datePattern);
		Matcher matcher = pattern.matcher(date);

		if (matcher.find()) {
			LOGGER.info("Date is in right format: " + date);
			return true;
		}

		LOGGER.info("Date is not in right format:  " + date);
		return false;
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

	private Boolean isDateThatNotExistInDB(String menuDate, String state, String existingDate) {

		if (state.equals(MenuStates.NEW.toString())) {
			if (isDateExist(menuDate)) {
				return false;
			}
		}

		if (state.equals(MenuStates.SAVED.toString())) {
			if (existingDate.equals(menuDate)) {
				return true;
			} else {
				if (isDateExist(menuDate)) {
					return false;
				}
			}
		}

		return true;
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

	private Boolean isDateExist(String menuDate) {

		List<Menu> menuList = getAllMenusFromTable();

		for (Menu menu : menuList) {
			if (menu.getDate().equals(menuDate)) {
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

	private List<Menu> getAllMenusFromTable() {

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

	private static boolean isDateGreaterThanToday(String menuDate) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar today = Calendar.getInstance();

		String curDate = dateFormat.format(today.getTime());

		if (curDate.compareTo(menuDate) < 0) {
			return true;
		}

		return false;
	}
}
