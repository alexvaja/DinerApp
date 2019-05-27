package dinerapp.controller;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpSession;
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
import dinerapp.model.entity.Dish;
import dinerapp.model.entity.Food;
import dinerapp.model.entity.Menu;
import dinerapp.model.entity.Order;
import dinerapp.model.entity.OrderQuantity;
import dinerapp.model.entity.UserDiner;
import dinerapp.repository.FoodRepository;
import dinerapp.repository.MenuRepository;
import dinerapp.repository.OrderQuantityRepository;
import dinerapp.repository.OrderRepository;
import dinerapp.repository.UserCantinaRepository;

@Controller
public class SelectionController {	
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private UserCantinaRepository userRepository;
	@Autowired
	private OrderQuantityRepository orderQuantityRepository;
	@Autowired
	private FoodRepository foodRepository;

	@SessionScope
	@GetMapping("/employeeOrderView")
	public String getAllData(HttpSession session, Model model) throws ParseException {	
		session.setAttribute("menuViewModel", new MenuViewModel());	
		// adds an attribute to the model that tells if a menu date has been picked or not
		model.addAttribute("isMenuDatePicked", false);
		// tests if the 'name' parameter is set in URL and if there is an user with that name
		String nameFromURL = (String) session.getAttribute("nameFromURL");

		if (nameFromURL != null) {
			// finds the user with the name from URL
			Optional<UserDiner> user = userRepository.findById(this.getUserIdByName(nameFromURL));
			// updates the list of available menu dates for user to pick from
			this.updateAvailableMenuDatesForUser(user.get(), model);
		}
		return "views/employeeOrderView";
	}

	@SessionScope
	@PostMapping("/employeeOrderView")
	public String getAllData(Model model, HttpSession session, @SessionAttribute(required = false) MenuViewModel menuViewModel,
			@RequestParam(value = "pickedDate", required = false) String pickedDate,
			@RequestParam(value = "submit", required = false) String actionType,
			@RequestParam(value = "quantity", required = false) String foodsQuantities,
			@RequestParam(value = "dishCheckbox", required = false) String dishIds,
			@RequestParam(value = "userNameFromPortal", required = false) String userNameFromPortal,
			@RequestParam(value = "date", required = false) String dateOfOrder) throws ParseException {
		
		// tests if there is any date picked
		if (pickedDate != null) {
			Menu menu = getMenuByDate(pickedDate);
			// sets data on menuViewModel
			menuViewModel.getMenuDTO().setDate(menu.getDate());
			menuViewModel.getMenuDTO().setTitle(menu.getTitle());
			menuViewModel.setDishesDTO(convertDishesToDishesDTO(menu.getDishes()));
		}
		Optional<UserDiner> user;
		// tests if username has been added to session
		if(session.getAttribute("nameFromURL") != null) {
			// gets user by username from session
			user = userRepository.findById(this.getUserIdByName((String)session.getAttribute("nameFromURL")));
		}
		else {
			// gets user by username from atos portal
			user = userRepository.findById(this.getUserIdByName(userNameFromPortal));
		}
		
		// actions to occur if a submit button is pressed
		if(actionType != null) {
			switch (actionType) {
				case "Comanda mancare": {
					// this button is on atos portal
					// sets on session username from atos portal
					session.setAttribute("nameFromURL", userNameFromPortal);
					return "redirect:/employeeOrderView";
				}
				case "Comanda": {
					List<String> foodQuantitiesAsList = new ArrayList<>();
					if(foodsQuantities == null) {
						model.addAttribute("noFoodSelected", true);
						model.addAttribute("isMenuDatePicked", true);
						return "views/employeeOrderView";
					}
					else {
						foodQuantitiesAsList = new ArrayList<>(Arrays.asList(foodsQuantities.split(",")));
						// tests if any dish has been selected; dishIds is a string of ids comma separated			
						if(this.areAllQuantitiesZero(foodQuantitiesAsList)) {
							model.addAttribute("noFoodSelected", true);
							model.addAttribute("isMenuDatePicked", true);
							return "views/employeeOrderView";
						}
					}

					// tests if there is already an order for given date and user
					if(isDateAlreadyOrderedForUser(dateOfOrder, user.get())){
						model.addAttribute("alreadyOrderedForThisDate", true);
						model.addAttribute("isMenuDatePicked", false);
						return "redirect:/employeeOrderView";
					}
								
					if(dishIds != null && !isDateAlreadyOrderedForUser(dateOfOrder, user.get())) {
						// adds a new order to database
						Order orderToAdd = this.addNewOrder(user.get(), dateOfOrder);
						// converts the comma separated string into a list of strings
						List<String> dishesIds = Arrays.asList(dishIds.split(","));
						// gets all foods ids for a dish
						List<String> foodIds = this.getFoodsForDish(dishesIds, dateOfOrder);
						// creates a map of foods and quantities
						Map<String, String> foodQuantities = this.mergeTwoListsIntoMap(foodIds, foodQuantitiesAsList);
						// adds a new orderQuantity to database
						this.addNewOrderQuantity(foodQuantities, orderToAdd);					
						model.addAttribute("orderedWithSuccess", true);
						}	
					return "redirect:/employeeOrderView";
				}
				case "Reseteaza":
					// resets all inputs to default value
					break;
				case "Anuleaza":
					return "redirect:/employeeOrderView";
				case "Alege data":
					// if there is no date to select and 'alege data' is pressed no action will be taken
					if(this.getAllAvailableMenuDatesForUser(user.get()).size() == 0) {
						model.addAttribute("isMenuDatePicked", false);
						break;
					}
					
					if(pickedDate == null) {
						return "redirect:/employeeOrderView";
					}
					
					if(!isDateAlreadyOrderedForUser(pickedDate, user.get())){
						if((LocalDate.now().isEqual(LocalDate.parse(pickedDate).minus(Period.ofDays(1))) 
								&& LocalTime.now().getHour() >= 16)
								|| LocalDate.now().isEqual(LocalDate.parse(pickedDate))) {
							model.addAttribute("insecureOrder", true);
						}
						model.addAttribute("isMenuDatePicked", true);
					}
					else {
						model.addAttribute("alreadyOrderedForThisDate", true);
						model.addAttribute("isMenuDatePicked", false);	
						return "redirect:/employeeOrderView";
					}
			}
		}
		return "views/employeeOrderView";
	}	
	
	private boolean areAllQuantitiesZero(List<String> quantities){
		for(String quantity : quantities) {
			if(!quantity.equals("0")) {
				return false;
			}
		}
		return true;
	}
	
	// adds a new order to database
	private Order addNewOrder(UserDiner user, String date) {
		Order order = new Order();
		order.setUserDiner(user);
		order.setDate(date);
		order.setTaken(false);
		order.setHour("00:00");
		if((LocalDate.now().isEqual(LocalDate.parse(date).minus(Period.ofDays(1))) 
				&& LocalTime.now().getHour() >= 16)
				|| LocalDate.now().isEqual(LocalDate.parse(date))) {
			
			String minutes = Integer.toString(LocalTime.now().getMinute());
			// if hour is 5 it will be 05
			if(minutes.length() == 1) {			
				minutes = "0" + minutes;
			}
			order.setHour(LocalTime.now().getHour() + ":" + minutes);
		}
		orderRepository.save(order);
		return order;
	}
	
	// adds a new orderQuantity into database
	private void addNewOrderQuantity(Map<String, String> foodQuantities, Order order) {
		for(Map.Entry<String, String> foodQuantity : foodQuantities.entrySet()) {
			// gets the food with the id from map 
			Optional<Food> food = foodRepository.findById(Integer.parseInt(foodQuantity.getKey()));
			// gets the quantity for food
			Integer quantity = Integer.parseInt(foodQuantity.getValue());		
			// adds a new OrderQuantity to database
			orderQuantityRepository.save(new OrderQuantity(order, food.get(), quantity));
		}	
	}
	
	// gets a list of all menu's dates that are after the current date
	private List<String> getAllAvailbleMenuDates() throws ParseException {
		Iterable<Menu> allMenusFromDB = menuRepository.findAll();
		List<String> avaialbeMenuDates = new ArrayList<>();
		for (Menu menu : allMenusFromDB) {
			// gets the date for current menu
			LocalDate menuDate = LocalDate.parse(menu.getDate());
			// tests if the date hasn't passed
			if ((menuDate.isAfter(LocalDate.now()) || menuDate.isEqual(LocalDate.now())) && menu.getState().trim().equals(MenuStates.PUBLISHED.toString().trim())) {
				// adds the date to available dates
				avaialbeMenuDates.add(menu.getDate());
			}
		}
		return avaialbeMenuDates;
	}
	
	// gets the list of all dates in which the user didn't order
	private List<String> getAllAvailableMenuDatesForUser(UserDiner user) throws ParseException {
		// gets a list of all available menus
		List<String> allMenuDates = this.getAllAvailbleMenuDates();
		List<String> alreadyOrderedDates = new ArrayList<>();
		for (Order order : orderRepository.findAll()) {
			// tests if the current order is associated with the given user
			if (order.getUserDiner().equals(user)) {
				// add the date of the order to the list of already ordered dates
				alreadyOrderedDates.add(order.getDate());
			}
		}
		// iterates through all already ordered dates
		for (String orderedDate : alreadyOrderedDates) {
			if (allMenuDates.contains(orderedDate)) {
				// removes the ordered date from allMenuDates
				allMenuDates.remove(orderedDate);
			}
		}
		// sorts all dates  
		Collections.sort(allMenuDates);	
		return allMenuDates;
	}
	
	// gets an user id based on his name
	private Integer getUserIdByName(String name) {
		for (UserDiner user : userRepository.findAll()) {
			// tests if there is an user with the given name
			if (user.getName().toUpperCase().trim().equals(name.toUpperCase().trim())) {
				return user.getId();
			}
		}
		return null;
	}
	
	// gets from database the menu with given date
	private Menu getMenuByDate(String date) {
		// finds the menu with the given date
		for (Menu menu : menuRepository.findAll()) {
			if (menu.getDate().equals(date)) {
				return menu;
			}
		}
		return null;
	}
	
	// gets all foods for a dish
	private List<String> getFoodsForDish(List<String> dishesIds, String date){
		List<String> foods = new ArrayList<>();
		for(String dishId : dishesIds) {
			// gets the selected menu
			Menu menu = getMenuByDate(date);
			// iterates through all dishes for selected menu date
			for(Dish dish : menu.getDishes()) {
				// tests if the category of the current dish is equals with categotyId
				if(dish.getCategory().getId() == Integer.parseInt(dishId)) {
					// iterates through all foods for current dish
					for(Food food: dish.getFoods()) {
						foods.add(food.getId().toString());
					}
				}		
			}
		}
		return foods;
	}
	
	// tests if a certain order already exists in database
	private boolean isDateAlreadyOrderedForUser(String date, UserDiner user) {
		for(Order order : orderRepository.findAll()) {
			// tests if current order already exists
			if(order.getDate().trim().equals(date.trim()) && order.getUserDiner().equals(user)) {
				return true;
			}
		}
		return false;
	}
		
	// converts a list of dishes to a list of dishesDTO
	private List<DishDTO> convertDishesToDishesDTO(List<Dish> dishes) {
		List<DishDTO> dishesDTO = new ArrayList<>();
		for (Dish dish : dishes) {
			DishDTO dishDTO = new DishDTO();
			List<CategoryDTO> categoriesDTO = new ArrayList<>();
			List<FoodDTO> foodsDTO = new ArrayList<>();
			// gets the categoryDTO for current dish
			CategoryDTO categoryDTO = new CategoryDTO(dish.getCategory(), false);
			// adds the categoryDTO to the list of categoriesDTO
			categoriesDTO.add(categoryDTO);
			// iterates through the list of foods for current dish
			for (Food food : dish.getFoods()) {
				// gets the foodDTO for current food
				FoodDTO foodDTO = new FoodDTO(food, false);
				// adds the foodDTO to the list of foodsDTO
				foodsDTO.add(foodDTO);
			}
			// sets the categories for dishDTO
			dishDTO.setCategories(categoriesDTO);
			// sets the foods for dshDTO
			dishDTO.setFoods(foodsDTO);
			// adds the dishDTO to the list of dishesDTO	
			dishesDTO.add(dishDTO);
		}
		return dishesDTO;
	}

	// updates all available menu dates for current user
	private void updateAvailableMenuDatesForUser(UserDiner user, Model model) throws ParseException {
		// gets all available menu dates for current user
		List<String> allAvailableDates = this.getAllAvailableMenuDatesForUser(user);
		// adds all available menu dates on model
		model.addAttribute("allAvailableMenuDates", allAvailableDates);
	}
	
	// creates a map from two lists
	private Map<String, String> mergeTwoListsIntoMap(List<String> foods, List<String> quantities){
		Map<String, String> foodQuantities = new HashMap<String, String>();
		// iterates through foodQuantities map
		for (int i = 0; i < foods.size(); i++) {
			// tests if there are quantities with value 0
			if(!quantities.get(i).equals("0")) {
				// tests if current key doesn't exist
				if (foodQuantities.get(foods.get(i)) == null) {
					// adds a new entry into map
					foodQuantities.put(foods.get(i), quantities.get(i));
				} else {
					// gets the existing quantity for that food
					Integer existingQuantity = Integer.parseInt(foodQuantities.get(foods.get(i)));
					// gets the quantity that has been added
					Integer addedQuantity = Integer.parseInt(quantities.get(i));
					// adds the two quantities previously calculated
					Integer finalQuantity = existingQuantity + addedQuantity;
					// adds a new entry into map
					foodQuantities.put(foods.get(i), finalQuantity.toString());
				}
			}		
		}			
		return foodQuantities;
	}
}
