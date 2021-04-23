package dinerapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dinerapp.constants.MenuStates;
import dinerapp.exceptions.NewSessionException;
import dinerapp.model.EditMenuViewModel;
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
import dinerapp.security.utils.DateComparer;

@Controller
public class ViewMenuController {

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

	@GetMapping("/viewMenuView")
	public String getController(Model model, HttpSession session) throws NewSessionException {

		LOGGER.info("-----------------------------------------------");
		LOGGER.info("CLASS NAME: " + MenuController.class.getName());
		LOGGER.info("-- GET METHOD --");
		
		if (session.isNew()) {
			throw new NewSessionException();
		}

		EditMenuViewModel editMenuViewModel = new EditMenuViewModel();
		List<Menu> listOfMenusFromTable = getAllMenusFromTable();
		List<Menu> listOfUnpublishedMenus = new ArrayList<>();

		for (Menu menu : listOfMenusFromTable) {
			if (menu.getState().equals(MenuStates.SAVED.toString())) {
				listOfUnpublishedMenus.add(menu);
			}
		}
		editMenuViewModel.setMenus(listOfUnpublishedMenus);

		model.addAttribute("editMenuViewModel", editMenuViewModel);
		model.addAttribute("isEmptyMenu", false);

		return "views/viewMenuView";
	}

	@PostMapping("/viewMenuView")
	public String setController(HttpSession session, Model model, @RequestParam MultiValueMap<String, String> params) {
		
		LOGGER.info("-----------------------------------------------");
		LOGGER.info("-- CLASS NAME: " + MenuController.class.getName() + " --");
		LOGGER.info("-- POST METHOD --");
		
		
		model.addAttribute("isEmptyMenu", false);
		EditMenuViewModel editMenuViewModel = new EditMenuViewModel();
		List<Menu> listOfMenusFromTable = getAllMenusFromTable();
		List<Menu> listOfUnpublishedMenus = new ArrayList<>();

		for (Menu menu : listOfMenusFromTable) {
			if (menu.getState().equals(MenuStates.SAVED.toString())) {
				listOfUnpublishedMenus.add(menu);
			}
		}

		editMenuViewModel.setMenus(listOfUnpublishedMenus);
		String idMenu = null;

		for (String key : params.keySet()) {
			idMenu = key;
		}

		String reqParam = params.getFirst(idMenu);

		switch (reqParam) {
		case "Editeaza": {
			LOGGER.info("ViewMenuController - Edit case");

			List<DishDTO> dishes = new ArrayList<>();

			Menu menu = editMenuViewModel.getMenus().get(Integer.parseInt(idMenu));

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
			menuDTO.setDate(menu.getDate());
			menuDTO.setState(menu.getState());


			menuDTO.setTitle(menu.getTitle());
			menuViewModel.setMenuDTO(menuDTO);

			menuViewModel.setDishesDTO(dishes);

			session.setAttribute("menuViewModel", menuViewModel);
			model.addAttribute("addMenuIsAvailable", true);
			return "views/menuView";
		}
		case "Publica":

			Menu menu = editMenuViewModel.getMenus().get(Integer.parseInt(idMenu));

			if (!editMenuViewModel.getMenus().isEmpty() && isEmptyCategoryMenu(menu)) {

				editMenuViewModel.getMenus().remove(menu);
				menu.setState(MenuStates.PUBLISHED.toString());
				menuRepository.save(menu);
			} else {
				model.addAttribute("isEmptyMenu", true);
			}

			break;
		}

		model.addAttribute("editMenuViewModel", editMenuViewModel);

		return "views/viewMenuView";
	}

	private boolean isEmptyCategoryMenu(Menu menu) {

		if (menu.getDishes().isEmpty())
			return false;
		return true;
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
		sortMenusByDate(searchedList);
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

	private void sortMenusByDate(List<Menu> menus) {
		Collections.sort(menus, new DateComparer());
	}
}