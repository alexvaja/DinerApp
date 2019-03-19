package dinerapp.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

import dinerapp.model.MenuViewModel;
import dinerapp.model.dto.CategoryDTO;
import dinerapp.model.dto.DishDTO;
import dinerapp.model.dto.FoodDTO;
import dinerapp.model.entity.Dish;
import dinerapp.model.entity.Food;
import dinerapp.model.entity.Menu;
import dinerapp.model.entity.Order;
import dinerapp.model.entity.OrderQuantity;
import dinerapp.model.entity.Role;
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
	public String getAllData(HttpSession session, Model model,
			@RequestParam(value = "name", required = false) String nameFromURL) throws ParseException {	
		// sets menuViewModel attribute on session
		session.setAttribute("menuViewModel", new MenuViewModel());	
		// adds an attribute to the model that tells if a menu date has been picked or not
		model.addAttribute("isMenuDatePicked", false);
		// tests if the 'name' parameter is set in URL and if there is an user with that name
		if (nameFromURL != null) {
			String username = convertHexToAscii(nameFromURL);
			// finds the user with the name from URL
			Optional<UserDiner> user = userRepository.findById(this.getUserIdByName(username));
//			if(!this.isUserEmployee(user.get())) {
//				return "views/notAuthorized";
//			}
			// sets the name taken from URL into session
			session.setAttribute("nameFromUrl", user.get());		
			// updates the list of available menu dates for user
			this.updateAvailableMenuDatesForUser(user.get(), model);
		}else {	
			// tests if user name is set on session
			if(session.getAttribute("nameFromUrl") != null) {
				// gets the user from session
				UserDiner user = (UserDiner) session.getAttribute("nameFromUrl");
				// updates the list of available menu dates for user
				this.updateAvailableMenuDatesForUser(user, model);
			}		
		}
		return "views/employeeOrderView";
	}

	@SessionScope
	@PostMapping("/employeeOrderView")
	public String getAllData(Model model, HttpSession session, @SessionAttribute MenuViewModel menuViewModel,
			@RequestParam(value = "pickedDate", required = false) String pickedDate,
			@RequestParam(value = "submit", required = false) String actionType,
			@RequestParam(value = "quantity", required = false) String foodsQuantities,
			@RequestParam(value = "dishCheckbox", required = false) String dishIds) throws ParseException {
		
		// tests if there is any date picked
		if (pickedDate != null) {
			// gets the menu for picked date
			Menu menu = getMenuByDate(pickedDate);
			// adds current picked date on session
			session.setAttribute("currentPickedDate", pickedDate);
			// sets data on menuViewModel
			menuViewModel.getMenuDTO().setDate(menu.getDate());
			menuViewModel.getMenuDTO().setTitle(menu.getTitle());
			menuViewModel.setDishesDTO(convertDishesToDishesDTO(menu.getDishes()));
		}
		// finds the user with name taken from URL
		UserDiner user = (UserDiner) session.getAttribute("nameFromUrl");
		// actions to occur if Submit button is pressed
		switch (actionType) {
			case "Comanda": {				
				// gets date from session
				String date = session.getAttribute("currentPickedDate").toString();
				/* tests if any dish has been selected; dishIds is a string of ids comma separated	
				 * tests if there is already an order for given date and user
				 */
				if(dishIds != null && !isDateAlreadyOrderedForUser(date, user)) {	
					// adds a new order to database
					this.addNewOrder(user, date);
					// converts the comma separated string into a list of strings
					List<String> dishesIds = Arrays.asList(dishIds.split(","));
					// gets all foods ids for a dish
					List<String> foodIds = this.getFoodsForDish(dishesIds, date);
					// converts quantities from comma separated string to list
					List<String> quantityIds = new ArrayList<>(Arrays.asList(foodsQuantities.split(",")));
					// creates a map of foods and quantities
					Map<String, String> foodQuantities = this.mergeTwoListsIntoMap(foodIds, quantityIds);
					// gets an order by date
					Order order = this.getOrderByDateAndUser(date, user);
					// adds a new orderQuantity to database
					this.addNewOrderQuantity(foodQuantities, order);
					}	
					
				// updates available menu dates for user
				this.updateAvailableMenuDatesForUser(user, model);
				// sets isMenuDatePicked to false
				model.addAttribute("isMenuDatePicked", false);
				break;
			}
			case "Reseteaza":
				// resets all inputs to default value
				break;
			case "Anuleaza":
				// updates available menu dates for user
				this.updateAvailableMenuDatesForUser(user, model);
				// sets isMenuDatePicked to false
				model.addAttribute("isMenuDatePicked", false);
				break;
			case "Alege data":
				// sets isMenuDatePicked to true
				model.addAttribute("isMenuDatePicked", true);
				List<String> allAvailableDates = this.getAllAvailableMenuDatesForUser(user);
				if(allAvailableDates.isEmpty()) {
					model.addAttribute("isMenuDatePicked", false);
				}

				break;
		}

		return "views/employeeOrderView";
	}	
	
	// adds a new order to database
	private void addNewOrder(UserDiner user, String date) {
		// creates a new order
		Order order = new Order();
		// set the order user with the given user
		order.setUserDiner(user);
		// set the order date with the given date
		order.setDate(date);
		// set taken attribute to false
		order.setTaken(false);
		// inserts the order into database
		orderRepository.save(order);
	}
	
	// adds a new orderQuantity into database
	private void addNewOrderQuantity(Map<String, String> foodQuantities, Order order) {
		// iterates through the map of foods and quantities
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
		// gets all menus from database
		Iterable<Menu> allMenusFromDB = menuRepository.findAll();
		List<String> avaialbeMenuDates = new ArrayList<>();
		// defines the format of the date
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		// gets the current date as string
		String currentDateAsString = dateFormat.format(Calendar.getInstance().getTime());
		// converts the current date from string to date
		Date currentDate = dateFormat.parse(currentDateAsString);
		// iterates through all menus from database
		for (Menu menu : allMenusFromDB) {
			// gets the date for current menu
			Date menuDate = dateFormat.parse(menu.getDate());
			// tests if the date hasn't passed
			if (menuDate.after(currentDate)) {
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
		// gets all orders from database
		Iterable<Order> allOrdersFromDB = orderRepository.findAll();
		// iterates through all orders
		for (Order order : allOrdersFromDB) {
			// tests if the current order is associated with the given user
			if (order.getUserDiner().getId() == user.getId()) {
				// add the date of the order to the list of already ordered dates
				alreadyOrderedDates.add(order.getDate());
			}
		}
		// iterates through all already ordered dates
		for (String orderedDate : alreadyOrderedDates) {
			// tests if the ordered date can be found in allMenuDates
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

		// gets all users from database
		Iterable<UserDiner> allUsersFromDB = userRepository.findAll();
		// iterates through all users
		for (UserDiner user : allUsersFromDB) {
			// tests if there is an user with the given name
			if (user.getName().toUpperCase().trim().equals(name.toUpperCase().trim())) {
				// returns the id of that user
				return user.getId();
			}
		}
		return null;
	}
	
	// gets an order by date
	private Order getOrderByDateAndUser(String date, UserDiner user) {
		// gets all orders from database
		Iterable<Order> allOrdersFromDB = orderRepository.findAll();
		// iterates through all orders
		for(Order order : allOrdersFromDB) {
			// tests if current order has the same date as given date
			if(order.getDate().equals(date) && order.getUserDiner().getId() == user.getId()) {
				return order;
			}
		}
		return null;
	}
	
	// gets from database the menu with given date
	private Menu getMenuByDate(String date) {
		// gets all menus from DB
		Iterable<Menu> allMenus = menuRepository.findAll();
		// finds the menu with the given date
		for (Menu menu : allMenus) {
			if (menu.getDate().equals(date)) {
				return menu;
			}
		}
		return null;
	}
	
	// gets all foods for a dish
	private List<String> getFoodsForDish(List<String> dishesIds, String date){
		// creates an empty list of strings
		List<String> foods = new ArrayList<>();
		// iterates through all dishes ids
		for(String dishId : dishesIds) {
			// gets the selected menu
			Menu menu = getMenuByDate(date);
			// iterates through all dishes for selected menu date
			for(Dish dish : menu.getDishes()) {
				// tests if the category of the current dish is equals with categotyId
				if(dish.getCategory().getId() == Integer.parseInt(dishId)) {
					// iterates through all foods for current dish
					for(Food food: dish.getFoods()) {
						// adds current food to foods
						foods.add(food.getId().toString());
					}
				}		
			}
		}
		return foods;
	}
	
	// tests if a certain order already exists in database
	private boolean isDateAlreadyOrderedForUser(String date, UserDiner user) {
		// gets all orders from database
		Iterable<Order> allOrdersFromDB = orderRepository.findAll();
		// iterates through all orders
		for(Order order : allOrdersFromDB) {
			// tests if current oder already exists
			if(order.getDate().equals(date) && order.getUserDiner().getId() == user.getId()) {
				return true;
			}
		}
		return false;
	}
	
	// tests if the user is employee or not
	private boolean isUserEmployee(UserDiner user){	
		List<Role> roles = user.getRoles();
		for(Role role : roles) {
			if(role.getName().equals("employee")) {
				return true;
			}
		}
		return false;
	}
	
	// parses the name taken from URL from hex to string
	private String convertHexToAscii(String hexString) {
	    StringBuilder output = new StringBuilder(""); 
		for (int i = 0; i < hexString.length(); i += 2) {
		     String str = hexString.substring(i, i + 2);
		     output.append((char) Integer.parseInt(str, 16));
		}	     
		return output.toString();
	}
		
	// converts a list of dishes to a list of dishesDTO
	private List<DishDTO> convertDishesToDishesDTO(List<Dish> dishes) {
		List<DishDTO> dishesDTO = new ArrayList<>();
		// iterates through the list of dishes
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
		// creates and empty map
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
