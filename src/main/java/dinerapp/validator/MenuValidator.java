package dinerapp.validator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dinerapp.constants.MenuStates;
import dinerapp.model.MenuViewModel;
import dinerapp.model.dto.CategoryDTO;
import dinerapp.model.dto.DishDTO;
import dinerapp.model.dto.FoodDTO;
import dinerapp.model.dto.MenuDTO;
import dinerapp.model.entity.Category;
import dinerapp.model.entity.Food;
import dinerapp.model.entity.Menu;
import dinerapp.repository.MenuRepository;

@Component("validator")
public class MenuValidator { //TODO LOGGER
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MenuRepository menuRepository;

	private String errorMessage;
	
	public MenuValidator() {
		super();
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String dateValidator(MenuViewModel menuViewModel) {
		
		setErrorMessage(null);
		LOGGER.info("-----------------------------------------------");
		LOGGER.info("-- MenuValidator -> dateValidator --");
		String menuDate = menuViewModel.getMenuDTO().getDate();
		
		if (menuDate.isEmpty()) {
			setErrorMessage("Data trebuie completata!");
			LOGGER.error(getErrorMessage());
			return getErrorMessage();
		}

		if (!isDateInRightFormat(menuDate)) {
			setErrorMessage("Data nu este in formatul potrivit!");
			LOGGER.error(getErrorMessage());
			return getErrorMessage();
		}

		if (!isDateGreaterThanToday(menuDate)) {
			setErrorMessage("Nu se poate adauga meniu pe ziua curenta!");
			LOGGER.error(getErrorMessage());
			return getErrorMessage();
		}
		
		if (!dateIsOK(menuViewModel)) {
			setErrorMessage("Exista un meniu deja pe acesta data!");
			LOGGER.error(getErrorMessage());
			return getErrorMessage();
		}
		
		return getErrorMessage();
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


	private Boolean dateIsOK(MenuViewModel menuViewModel) {
		
		MenuDTO menuDTO = menuViewModel.getMenuDTO();
		
		if (menuDTO.getState().equals(MenuStates.NEW.toString())) {
			System.out.println("Stare => NEW");
			if (isDateExist(menuDTO.getDate())) {
				return false;
			}
		}
		
		if (menuDTO.getState().equals(MenuStates.SAVED.toString())) {
			System.out.println("Stare => SAVED");
			Optional<Menu> menu = menuRepository.findById(menuDTO.getId());
			System.out.println("Meniul din DB: " + menu.get());
			if (!menuDTO.getDate().equals(menu.get().getDate())) {
				if (isDateExist(menuDTO.getDate())) {
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



	private List<Menu> getAllMenusFromTable() {

		Iterable<Menu> list = menuRepository.findAll();
		List<Menu> searchedList = new ArrayList<>();

		for (Menu menu : list) {
			searchedList.add(menu);
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
