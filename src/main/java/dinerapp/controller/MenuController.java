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
		LOGGER.error("Incercare de acces nepermis");
		return "views/loginView";
	}

	@ExceptionHandler({ DuplicateCategoryException.class })
	public String duplicateError() {
		LOGGER.error("Sunt doua sau mai multe categorii dublicate!");
		return "redirect:menuView";
	}

	@ExceptionHandler({ WrongMenuDateException.class })
	public String dateError() {
		LOGGER.error("Data nu este potrivita");
		return "redirect:menuView";
	}

	@SessionScope
	@GetMapping("/menuView")
	public String getMethod(Model model, Principal principal, HttpSession session) throws NewSessionException {
		
		System.err.println("AM INTRAT PE GET");
		
		LOGGER.info("Am intrat pe GetsMapping -> MenuController");

		if (session.isNew()) {
			throw new NewSessionException();
		}

		//LOGGER.info("VM de pe sesiune: " + session.getAttribute("menuViewModel"));
		model.addAttribute("addMenuIsAvailable", false);
		
		if (session.getAttribute("menuViewModel") == null) {
			
			System.err.println("AM INTRAT PE IF");
			session.removeAttribute("menuViewModel"); //optional
			session.setAttribute("menuViewModel", new MenuViewModel());
			session.setAttribute("errorMessage", false);
			session.setAttribute("dateErrorMessage", false);

			//Boolean addMenuIsAvailable = false;
			model.addAttribute("addMenuIsAvailable", false);
		} else {
			System.err.println("AM INTRAT PE ELSE");
			//System.err.println("ERROR MESSAGE: " + session.getAttribute("errorMessage"));
			//System.err.println("DATE ERROR MESSAGE: " + session.getAttribute("dateErrorMessage"));
			
			if(session.getAttribute("errorMessage").equals("true")) {
				session.setAttribute("dateErrorMessage", false);
			}
			
			if(session.getAttribute("dateErrorMessage").equals("true")) {
				session.setAttribute("errorMessage", false);
			}

			//Boolean addMenuIsAvailable = true;
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
		
		System.err.println("AM INTRAT PE POST");

		session.setAttribute("dateErrorMessage", false); //setez ororile pe false
		session.setAttribute("errorMessage", false); //setez erorile pe false
		{
			//LOGGER.info("Am intrat pe PostMapping -> MenuController");
			//LOGGER.info("MENU VIEW MODEL: " + menuViewModel);		
		}

		//Boolean addMenuIsAvailable = false;
		//model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);

		switch (reqParam) {
		case "Adauga Meniu": {
			
			{
				LOGGER.info("CLASS NAME" + MenuController.class.getName());
				LOGGER.info("METHOD NAME: " + "postMethod");
				System.err.println("-- ADAUGARE MENIU --");
				System.out.println("");
				System.err.println("reqParam: " + reqParam);
				System.err.println("menuTitle: " + menuTitle);
				System.err.println("menuDate:" + menuDate);
				System.err.println("selectedMenuCategories: " + selectedMenuCategories);
				//System.err.println("selectedMenuFoods: " + selectedMenuFoods);
				//System.err.println("VIEW MODEL: " + menuViewModel);
			} // convert to LOGGER

			model.addAttribute("addMenuIsAvailable", true);
			List<DishDTO> dishes = menuViewModel.getDishesDTO();
			dishes.add(createDefaultDishesDTO());
			MenuDTO menuDTO = new MenuDTO();
			menuDTO.setState(menuViewModel.getMenuDTO().getState());
			menuViewModel.setDishesDTO(dishes);
			menuViewModel.setMenuDTO(menuDTO);
			session.setAttribute("menuViewModel", menuViewModel);
			
			break;
		}
		case "Adauga Categorie Noua": {
			
			{
				LOGGER.info("CLASS NAME" + MenuController.class.getName());
				LOGGER.info("METHOD NAME: " + "postMethod");
				System.err.println("-- ADAUGARE CATEGORIE NOUA MENIU --");
				System.out.println();
				System.err.println("reqParam: " + reqParam);
				System.err.println("menuTitle: " + menuTitle);
				System.err.println("menuDate:" + menuDate);
				//System.err.println("selectedMenuCategories: " + selectedMenuCategories);
				//System.err.println("selectedMenuFoods: " + selectedMenuFoods);
			} // convert to LOGGER
			
			model.addAttribute("addMenuIsAvailable", true);
			List<DishDTO> dishes = menuViewModel.getDishesDTO();

			if (selectedMenuCategories != null) {
				updateListSelectedCategory(selectedMenuCategories, dishes);
			}

			if (selectedMenuFoods != null) {
				updateListSelectedFoods(selectedMenuFoods, dishes);
			}

			dishes.add(createDefaultDishesDTO());

			MenuDTO menuDTO = new MenuDTO();
			menuDTO.setId(menuViewModel.getMenuDTO().getId());
			menuDTO.setTitle(menuTitle);
			menuDTO.setDate(menuDate);
			menuDTO.setState(menuViewModel.getMenuDTO().getState());

			menuViewModel.setDishesDTO(dishes);
			menuViewModel.setMenuDTO(menuDTO);
			
			break;

		}
		case "Anuleaza": {
			{
				System.err.println("-- ANULARE MENIU --");
				System.out.println();
				System.err.println("reqParam: " + reqParam);
				System.err.println("menuTitle: " + menuTitle);
				System.err.println("menuDate:" + menuDate);
				//System.err.println("selectedMenuCategories: " + selectedMenuCategories);
				//System.err.println("selectedMenuFoods: " + selectedMenuFoods);
				//LOGGER.info("Am intrat pe Anuleaza -> MenuController");
			}  // convert to LOGGER
			
			model.addAttribute("addMenuIsAvailable", false);
			session.setAttribute("errorMessage", false);
			session.setAttribute("dateErrorMessage", false);
			//session.setAttribute("dateErrorMessage", false);
			session.removeAttribute("menuViewModel");
			System.err.println("MENU VIEW MODEL DUPA ANULARE!!!!!!!!!!!!!!!! " + session.getAttribute("menuViewModel"));
			session.setAttribute("menuViewModel", new MenuViewModel());
			//System.err.println("MENU VIEW MODEL DUPA ANULARE!!!!!!!!!!!!!!!! " + session.getAttribute("menuViewModel"));
			
			break;
		}
		case "Salvare": {
		
			{
				System.err.println("-- SALVARE MENIU --");
				System.out.println();
				System.err.println("DATA CARE ESTE PE FORMULAR: " + menuDate);
				System.err.println("SESIUNE: " + menuViewModel.getMenuDTO());
				System.err.println("reqParam: " + reqParam);
				System.err.println("menuTitle: " + menuTitle);
				System.err.println("menuDate:" + menuDate);
				//System.err.println("selectedMenuCategories: " + selectedMenuCategories);
				//System.err.println("selectedMenuFoods: " + selectedMenuFoods);
				//LOGGER.info("Am intrat pe Salvare -> MenuController");
				//LOGGER.info("Lista de category: " + getAllCategoriesFromMenu(menuViewModel.getDishesDTO()));
			} // convert to LOGGER
			
			//Menu menu = new Menu();
			//menu.setDate(menuDate);
			//menu.setTitle(menuTitle);
			
			MenuDTO menuDTO = new MenuDTO();
			menuDTO.setId(menuViewModel.getMenuDTO().getId());
			menuDTO.setTitle(menuTitle);
			menuDTO.setDate(menuDate);
			menuDTO.setState(menuViewModel.getMenuDTO().getState());
			menuViewModel.setMenuDTO(menuDTO);
			
			List<DishDTO> dishes = menuViewModel.getDishesDTO();
			
			if (selectedMenuCategories != null) { //fac update la categorii
				updateListSelectedCategory(selectedMenuCategories, dishes);
			}
			
			if (selectedMenuFoods != null) { //fac update la mancare
				updateListSelectedFoods(selectedMenuFoods, dishes);
			}
			
			if (menuDate.isEmpty()) { //verific ca data sa nu fie nula
				session.setAttribute("dateErrorMessage", true);
				throw new WrongMenuDateException("Date does not exist!");
			}

			if (!isDateInRightFormat(menuDate)) { //verific ca data sa aiba formaul bun
				session.setAttribute("dateErrorMessage", true);
				throw new WrongMenuDateException("Date does not have the right format!");
			}
			
			if (!isDateGreaterThanToday(menuDate)) { //verific ca data sa fie mai mare ca ziua curenta
				session.setAttribute("dateErrorMessage", true);
				throw new WrongMenuDateException("Date does not have the right format!");
			}

			//verific sa nu existe data venita din formular in baza de date + sa nu fie null
			if (isDateThatNotExistInDB(menuDate, menuViewModel.getMenuDTO().getState(), menuViewModel.getMenuDTO().getDate())) {

/*				if (selectedMenuCategories != null) {
					updateListSelectedCategory(selectedMenuCategories, dishes);
				}

				if (selectedMenuFoods != null) {
					updateListSelectedFoods(selectedMenuFoods, dishes);
				}*/

				if (!areNotDuplicateCategories(dishes)) {
					session.setAttribute("errorMessage", true);
					throw new DuplicateCategoryException("mesaj");
				} /*else {
					session.setAttribute("errorMessage", false);
				}*/
				
				
				//PARTEA UNU DE VALIDARI PROPRIU ZIS
				
				
				
				List<Dish> selectedDishList = new ArrayList<>();

				Menu menu = new Menu();
				menu.setId(menuViewModel.getMenuDTO().getId());
				menu.setDate(menuViewModel.getMenuDTO().getDate());
				menu.setTitle(menuViewModel.getMenuDTO().getTitle());
				menu.setState(MenuStates.SAVED.toString());
				
				menuRepository.save(menu);

				for (DishDTO dishDTO : dishes) {
					List<Food> selectedFoods = getSelectedFoodsForCategory(dishDTO.getFoods());

					LOGGER.info("Lista de mancarruri: " + dishDTO.getId());
					LOGGER.info(selectedFoods.toString());

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

				//addMenuIsAvailable = false;
				session.removeAttribute("menuViewModel"); // perfect!
			} else {
				session.setAttribute("dateErrorMessage", true);
				throw new WrongMenuDateException("There is a menu already saved on this date!");
			}

			return "redirect:/viewMenuView";
		}
		}

		//session.removeAttribute("menuViewModel");
		//model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
		return "views/menuView";
	}

	public MenuController(CategoryRepository categoryRepository, FoodRepository foodRepository,
			MenuRepository menuRepository, DishRepository dishRepository) {
		super();
		this.categoryRepository = categoryRepository;
		this.foodRepository = foodRepository;
		this.menuRepository = menuRepository;
		this.dishRepository = dishRepository;
	}

	private boolean isDateInRightFormat(String date) {

		String datePattern = "20\\d\\d-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])$";

		Pattern r = Pattern.compile(datePattern);
		Matcher m = r.matcher(date);

		if (m.find()) {
			LOGGER.info("Data are formatul bun => " + date);
			return true;
		}
		LOGGER.info("Data nu are formatul bun => " + date);
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
		
/*		if (menuDate.isEmpty()) {
			return false;
		}*/

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
				LOGGER.info("Categoria 1: " + categories.get(i).getName());
				LOGGER.info("Categoria 2: " + categories.get(j).getName());
				LOGGER.info("<---Mereu o sa crape le meniu--->");
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

	private static boolean isDateGreaterThanToday(String menuDate)
	{
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		Calendar todayDate = Calendar.getInstance();
		
		String curDate = dt.format(todayDate.getTime());
		
		if(curDate.compareTo(menuDate) < 0) {
			return true;
		}
				
		return false;
	}

}
