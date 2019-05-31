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
import dinerapp.model.dto.MenuDTO;
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
}
