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
import dinerapp.repository.CategoryRepository;
import dinerapp.repository.DishRepository;
import dinerapp.repository.FoodRepository;
import dinerapp.repository.MenuRepository;

@Component("validator")
public class MenuValidator { //TODO LOGGER
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private FoodRepository foodRepository;

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private DishRepository dishRepository;

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
	
	public String titleValidator(MenuViewModel menuViewModel) {
		
		LOGGER.info("-----------------------------------------------");
		LOGGER.info("-- MenuValidator -> titleValidator --");
		
		setErrorMessage(null);

		if (menuViewModel.getMenuDTO().getTitle().isEmpty()) {
			setErrorMessage("Titlul nu poate sa fie gol!");
			LOGGER.error(getErrorMessage());
			return getErrorMessage();
		}

		return getErrorMessage();
	}
	
	public String dateValidator(MenuViewModel menuViewModel) {
		
		LOGGER.info("-----------------------------------------------");
		LOGGER.info("-- MenuValidator -> dateValidator --");

		setErrorMessage(null);
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
	
	public String categoryValidator(MenuViewModel menuViewModel) {
		
		LOGGER.info("-----------------------------------------------");
		LOGGER.info("-- MenuValidator -> categoryValidator --");
		
		setErrorMessage(null);
		
		if (!areNotDuplicateCategories(menuViewModel.getDishesDTO())) {
			setErrorMessage("Categoriile trebuie sa fie diferite!");
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
			if (isDateExist(menuDTO.getDate())) {
				return false;
			}
		}
		
		if (menuDTO.getState().equals(MenuStates.SAVED.toString())) {
			Optional<Menu> menu = menuRepository.findById(menuDTO.getId());
			if (!menuDTO.getDate().equals(menu.get().getDate())) {
				if (isDateExist(menuDTO.getDate())) {
					return false;
				}
			}
		}
		
		return true;
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

	private List<Menu> getAllMenusFromTable() {

		Iterable<Menu> list = menuRepository.findAll();
		List<Menu> searchedList = new ArrayList<>();

		for (Menu menu : list) {
			searchedList.add(menu);
		}

		return searchedList;
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
	
	private List<Food> getSelectedFoodsForCategory(List<FoodDTO> savedFoods) {

		List<Food> selectedFoods = new ArrayList<>();

		for (FoodDTO foodDTO : savedFoods) {
			if (foodDTO.getSelected()) {
				selectedFoods.add(foodDTO.getFood());
			}
		}
		return selectedFoods;
	}
}
