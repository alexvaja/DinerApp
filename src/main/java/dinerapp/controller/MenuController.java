package dinerapp.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
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

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	FoodRepository foodRepository;

	@SessionScope
	@GetMapping("/menuView")
	public String sessionExample(Model model, Principal principal, HttpSession session) {

		LOGGER.info("GET MENU");
		session.setAttribute("menuViewModel", new MenuViewModel());

		Boolean addMenuIsAvailable = false;
		model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);

		return "menuView";
	}
	
	@Transactional
	@PostMapping("/menuView")
	public String setAllMenu(Model model, @SessionAttribute MenuViewModel menuViewModel,
			@RequestParam(value = "submit") String reqParam,
			@RequestParam(value = "menu_title", required = false) String menuTitle,
			@RequestParam(value = "menu_date", required = false) String menuDate,
			@RequestParam(value = "drop_down_list", required = false) String dropDownList,
			@RequestParam(value = "verif", required = false) String selectedMenuFood) {

		LOGGER.info("SET MENU");
		System.out.println("Vine param: " + reqParam);
		System.out.println("Categories:" + dropDownList);
		System.out.println("Foods: " + selectedMenuFood);

		Boolean addMenuIsAvailable = false;
		model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);

		switch (reqParam) {
		case "AddMenu":

			addMenuIsAvailable = true;
			model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);

			List<DishDTO> dishes = menuViewModel.getDishes();

			String[] indexFood = null;

			if (selectedMenuFood != null) {
				indexFood = selectedMenuFood.split(",");

				int i = 0;

				for (DishDTO dishDTO : dishes) {
					List<FoodDTO> listFood = dishDTO.getFoods();

					for (FoodDTO foodDTO : listFood) {
						foodDTO.setSelected(false);
					}

					while (Integer.valueOf(indexFood[i]) != -1) {
						listFood.get(Integer.valueOf(indexFood[i])).setSelected(true);
						i++;
					}

					i++;
					dishDTO.setFoods(listFood);
				}
			}

			String[] indexCategory = null;

			if (dropDownList != null) {
				indexCategory = dropDownList.split(",");

				int i = 0;

				for (DishDTO dishDTO : dishes) {
					List<CategoryDTO> listCategory = dishDTO.getCategories();

					for (CategoryDTO categoryDTO : listCategory) {
						categoryDTO.setSelected(false);
					}

					listCategory.get(Integer.parseInt(indexCategory[i])).setSelected(true);

					i++;
					dishDTO.setCategories(listCategory);
				}
			}

			dishes.add(getEmptyDish());
			menuViewModel.setDishes(dishes);
			menuViewModel.setTitle(menuTitle);
			menuViewModel.setDate(menuDate);

			break;
		case "Cancel":
			menuViewModel.getDishes().clear();
			break;
		case "SaveAll":
			System.out.println("MenuViewModel in Save");
			System.out.println(menuViewModel);
//			List<DishDTO> listDish = menuViewModel.getDishes();
//			for (DishDTO dish : listDish) {
//				List<CategoryDTO> savedCategory = dish.getCategories();
//				Category selectedCategory = null;
//				for (CategoryDTO category : savedCategory) {
//					if (category.getSelected() == true) {
//						selectedCategory = category.getCategory();
//						break;
//					}
//				}
//				categoryRepository.save(selectedCategory);
//				
//				List<FoodDTO> savedFood = dish.getFoods();
//				List<Food> selectedFood = null;
//				for(FoodDTO food : savedFood) {
//					if (food.getSelected() == true) {
//						selectedFood.add(food.getFood());
//					}
//				}
//				
//				foodRepository.saveAll(selectedFood);
//			}
			
			break;
		}
		
		return "menuView";
	}

	private void cevaFood(String[] indexFood) {

	}

	private DishDTO getEmptyDish() {
		List<Category> listOfCategories = getListOfCategoriesFromDB();
		List<Food> listOfFoods = getListOfFoodsFromDB();

		List<CategoryDTO> listOfCategoriesDTO = new ArrayList<>();
		List<FoodDTO> listOfFoodsDTO = new ArrayList<>();

		for (Category category : listOfCategories) {
			listOfCategoriesDTO.add(new CategoryDTO(category, false));
		}

		for (Food food : listOfFoods) {
			listOfFoodsDTO.add(new FoodDTO(food, false));
		}

		DishDTO dishDTO = new DishDTO();
		dishDTO.setCategories(listOfCategoriesDTO);
		dishDTO.setFoods(listOfFoodsDTO);

		return dishDTO;
	}

	private List<Category> getListOfCategoriesFromDB() {

		Iterable<Category> list = categoryRepository.findAll();
		List<Category> searchedList = new ArrayList<>();
		for (Category category : list) {
			searchedList.add(category);
		}
		return searchedList;
	}

	private List<Food> getListOfFoodsFromDB() {

		Iterable<Food> list = foodRepository.findAll();
		List<Food> searchedList = new ArrayList<>();
		for (Food food : list) {
			searchedList.add(food);
		}
		return searchedList;
	}

}