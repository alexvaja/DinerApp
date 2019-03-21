package dinerapp.controller;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
		LOGGER.error("incercare de acces nepermis");
		return "views/loginView";
	}

	@ExceptionHandler({ DuplicateCategoryException.class })
	public String duplicateError() {
		LOGGER.error("categorii duplicate");
		return "redirect:menuView";
	}
	
	@ExceptionHandler({ WrongMenuDateException.class })
	public String dateError() {
		LOGGER.error("exista aceasta data deja");
		return "redirect:menuView";
	}

	@SessionScope
	@GetMapping("/menuView")
	public String sessionExample(Model model, Principal principal, HttpSession session) throws NewSessionException {
		LOGGER.info("Am intrat pe GetsMapping -> MenuController");

		if (session.isNew()) {
			throw new NewSessionException();
		}
		
		LOGGER.info("VM de pe sesiune: " + session.getAttribute("menuViewModel"));
		
		if (session.getAttribute("menuViewModel") == null) {
			session.setAttribute("errorMessage", false);
			session.setAttribute("menuViewModel", new MenuViewModel());

			Boolean addMenuIsAvailable = false;
			model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
		} else {
			
			if (session.getAttribute("errorMessage") == null) {
				session.setAttribute("errorMessage", false);
			} else {
				session.setAttribute("errorMessage", true);
			}
			
			// vaja style
			Boolean addMenuIsAvailable = true;
			model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);

		}

		return "views/menuView";
	}

	@Transactional
	@PostMapping("/menuView")
	public String setAllMenu(Model model, HttpSession session, @SessionAttribute MenuViewModel menuViewModel,
			@RequestParam(value = "submit") String reqParam,
			@RequestParam(value = "menu_title", required = false) String menuTitle,
			@RequestParam(value = "menu_date", required = false) String menuDate,
			@RequestParam(value = "dropdown_list", required = false) String selectedMenuCategories,
			@RequestParam(value = "checkbox_list", required = false) String selectedMenuFoods)
			throws DuplicateCategoryException, ParseException, WrongMenuDateException {

		Boolean addMenuIsAvailable = false;
		model.addAttribute("add MenuIsAvailable", addMenuIsAvailable);
		
		LOGGER.info("Am intrat pe PostMapping -> MenuController");
		LOGGER.info("MENU VIEW MODEL: " + menuViewModel);
		
		switch (reqParam) {

		case "Adauga Meniu": {
	
			LOGGER.info("Am intrat pe Adauga Meniu -> MenuController");
			List<DishDTO> dishes = menuViewModel.getDishesDTO();

			addMenuIsAvailable = true;
			model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
			if (selectedMenuCategories != null) 
				updateListSelectedCategory(selectedMenuCategories, dishes);
			
			if (selectedMenuFoods != null) 
				updateListSelectedFoods(selectedMenuFoods, dishes);

				dishes.add(createDefaultDishesDTO());

				MenuDTO menuDTO = new MenuDTO();
				menuDTO.setState(menuViewModel.getMenuDTO().getState());//
				menuDTO.setId(menuViewModel.getMenuDTO().getId());
				menuDTO.setDate(menuDate);
				menuDTO.setTitle(menuTitle);

				menuViewModel.setDishesDTO(dishes);
				menuViewModel.setMenuDTO(menuDTO);
				break;
			
		}
		case "Adauga Categorie Noua":
		{
			LOGGER.info("Am intrat pe Adauga Categorie Noua -> MenuController");
			List<DishDTO> dishes = menuViewModel.getDishesDTO();

			addMenuIsAvailable = true;
			model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);

			if (selectedMenuCategories != null) {
				updateListSelectedCategory(selectedMenuCategories, dishes);
			}

			if (selectedMenuFoods != null) {
				updateListSelectedFoods(selectedMenuFoods, dishes);
			}		
				
				dishes.add(createDefaultDishesDTO());

				MenuDTO menuDTO = new MenuDTO();
				menuDTO.setState(menuViewModel.getMenuDTO().getState());//
				menuDTO.setId(menuViewModel.getMenuDTO().getId());
				menuDTO.setDate(menuDate);
				menuDTO.setTitle(menuTitle);

				menuViewModel.setDishesDTO(dishes);
				menuViewModel.setMenuDTO(menuDTO);
				// menuViewModel.setTitle(menuTitle);
				// menuViewModel.setDate(menuDate);

				break;
			
		}
		case "Anuleaza": {
			LOGGER.info("Am intrat pe Anuleaza -> MenuController");
			addMenuIsAvailable = false;
			
			session.setAttribute("errorMessage", false);
			session.removeAttribute("menuViewModel");
			session.setAttribute("menuViewModel", new MenuViewModel());
			break;
		}
		case "Salvare": {
			
			LOGGER.info("Am intrat pe Salvare -> MenuController");
			List<DishDTO> dishes = menuViewModel.getDishesDTO();
			LOGGER.info("Lista de category: " + getAllCategoriesFromMenu(dishes));
			
			if (selectedMenuCategories != null) {
				updateListSelectedCategory(selectedMenuCategories, dishes);
			}

			if (selectedMenuFoods != null) {
				updateListSelectedFoods(selectedMenuFoods, dishes);
			}

			
			if (canSave(menuDate, menuViewModel.getMenuDTO().getState(), menuViewModel.getMenuDTO().getDate())) {

				if (selectedMenuCategories != null) {
					updateListSelectedCategory(selectedMenuCategories, dishes);
				}

				if (selectedMenuFoods != null) {
					updateListSelectedFoods(selectedMenuFoods, dishes);
				}

				if (!isValid(dishes)) {
					session.setAttribute("errorMessage", true);
					throw new DuplicateCategoryException("mesaj");
				} else {
					session.setAttribute("errorMessage", false);
				}
				//

				List<Dish> selectedDishList = new ArrayList<>();

				Menu menu = new Menu();

				// aici pun id
				menu.setId(menuViewModel.getMenuDTO().getId());
				menu.setDate(menuDate);
				menu.setTitle(menuTitle);
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

				addMenuIsAvailable = false;
				session.removeAttribute("menuViewModel");
			} else {
				throw new WrongMenuDateException("mesaj");
			}

			return "redirect:/viewMenuView";
		}
		}

		model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
		return "views/menuView";
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

	private Boolean canSave(String menuDate, String state, String existingDate) {
		if (menuDate.isEmpty()) {
			return false;
		}

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

	private boolean isValid(List<DishDTO> dishes) {
		
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
}
