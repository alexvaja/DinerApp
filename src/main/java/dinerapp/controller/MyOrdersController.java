package dinerapp.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
import org.springframework.web.context.annotation.SessionScope;

import dinerapp.model.MyOrdersViewModel;
import dinerapp.model.dto.FoodDTO;
import dinerapp.model.dto.MenuDTO;
import dinerapp.model.dto.OrderDTO;
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
public class MyOrdersController {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private UserCantinaRepository userRepository;
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private FoodRepository foodRepository;
	@Autowired
	private OrderQuantityRepository orderQuantityRepository;
	@PersistenceContext
	private EntityManager entityManager;
		
	@SessionScope
	@GetMapping("/orders")
	public String getController(HttpSession session, Model model, MyOrdersViewModel myOrdersViewModel) {
		// gets user form session
		
		String nameFromURL = (String) session.getAttribute("nameFromURL");
		//
		//nameFromURL = "diana";//
		Optional<UserDiner> user = userRepository.findById(this.getUserIdByName(nameFromURL));
		model.addAttribute("allOrderedDates", this.getAllOrderedDatesForUser(user.get()));
		model.addAttribute("isDatePicked", false);
		return "views/myOrdersView";
	}

	@SessionScope
	@Transactional
	@PostMapping("/orders")
	public String postController(HttpSession session, Model model,
			@RequestParam(name = "orderId", required = false) String orderId,
			@RequestParam(name = "submit", required = false) String actionType,
			@RequestParam(name = "pickedDate", required = false) String pickedDate,
			@RequestParam(value = "date", required = false) String dateOfOrder,
			@RequestParam(value = "quantities", required = false) String quantities) {

		// gets user from session
		Optional<UserDiner> user = userRepository.findById(this.getUserIdByName((String) session.getAttribute("nameFromURL")));
		//String nameFromURL = "diana";//
		//Optional<UserDiner> user = userRepository.findById(this.getUserIdByName(nameFromURL));//
		
		MyOrdersViewModel myOrdersViewModel = new MyOrdersViewModel();
		String date = findCorrectDate(pickedDate, dateOfOrder);
		
		switch (actionType) {
			case "Vizualizeaza comanda": {
				// if there is no date to select and 'vizualizeaza comanda' is pressed no action will be taken
				if(this.getAllOrderedDatesForUser(user.get()).size() == 0) {
					model.addAttribute("isDatePicked", false);
					return "redirect:/orders";
				}
				loadCurrentPage(model, user, myOrdersViewModel, date);
				return "views/myOrdersView";
			}
			case "Sterge comanda": {
				removeOrder(Integer.parseInt(orderId));
				model.addAttribute("orderRemoved", true);
				model.addAttribute("isDatePicked", false);
				model.addAttribute("allOrderedDates", getAllOrderedDatesForUser(user.get()));
				return "views/myOrdersView";
			}
			case "Salveaza modificarile": {
				if(!this.areAllQuantitiesZero(quantities) && isOrderInDatabase(getOrderByUserAndDate(user.get(), date))) {
					//gets selected order
					Order selectedOrder = this.getOrderByUserAndDate(user.get(), date);
					// removes seleted order
					removeOrder(selectedOrder.getId());
					Order editedOrder = this.saveEditedOrder(selectedOrder, user.get());
					
					List<String> quantity = new ArrayList<>(Arrays.asList(quantities.split(",")));
					List<Food> foodsForOrder = this.convertFromFoodsDTOToFoods(this.getAllFoodsForMenu(this.getMenuByDate(date)));					
					
					Map<Food, Integer> foodQuantities = this.mergeTwoListsIntoMap(foodsForOrder, quantity);		
					
					for (Map.Entry<Food, Integer> entry : foodQuantities.entrySet()) {
						orderQuantityRepository.save(new OrderQuantity(editedOrder, entry.getKey(), entry.getValue()));				
					}
					
					model.addAttribute("orderWasModified", true);
					loadCurrentPage(model, user, myOrdersViewModel, date);
				}
				else {
					return "redirect:/orders";
				}		
				
				return "views/myOrdersView";				
			}
			case "Reseteaza": {
				loadCurrentPage(model, user, myOrdersViewModel, date);
				return "views/myOrdersView";
			}
			case "Anuleaza": {
				return "redirect:/orders";	
			}
		}
		return "redirect:/employeeOrderView";
	}
	
	private String findCorrectDate(String pickedDate, String dateOfOrder) {
		// dateOfOrded is used when isDatePicked = true
		String date = dateOfOrder;
		// pickedDate is used when isDatePicked = false
		if (pickedDate != null) {
			date = pickedDate;
		}
		return date;
	}

	private Order saveEditedOrder(Order selectedOrder, UserDiner user) {
		Order order = new Order(user, false, selectedOrder.getDate(), selectedOrder.getHour());			
		orderRepository.save(order);
		return order;
	}
	
	private boolean areAllQuantitiesZero(String quantity){
		List<String> quantities = new ArrayList<>(Arrays.asList(quantity.split(",")));
		for(String quantityObj : quantities) {
			if(!quantityObj.equals("0")) {
				return false;
			}
		}
		return true;
	}
	
	private void loadCurrentPage(Model model, Optional<UserDiner> user, MyOrdersViewModel myOrdersViewModel, String date) {
		model.addAttribute("allOrderedDates", this.getAllOrderedDatesForUser(user.get()));
		model.addAttribute("isDatePicked", true);
		myOrdersViewModel.setOrderDTO(getOrderDTOForDateAndUser(date, user.get()));
		model.addAttribute("myOrdersViewModel", myOrdersViewModel);
	}

	// creates a map from two lists
	private Map<Food, Integer> mergeTwoListsIntoMap(List<Food> foods, List<String> quantities) {
		Map<Food, Integer> foodQuantities = new LinkedHashMap<Food, Integer>();
		// iterates through foodQuantities map
		for (int i = 0; i < foods.size(); i++) {
			if(Integer.parseInt(quantities.get(i)) != 0) {
				foodQuantities.put(foods.get(i), Integer.parseInt(quantities.get(i)));
			}
		}
		return foodQuantities;
	}

	private Order getOrderByUserAndDate(UserDiner user, String date) {
		for (Order order : orderRepository.findAll()) {
			if (order.getUserDiner().equals(user) && order.getDate().equals(date)) {
				return order;
			}
		}
		return null;
	}

	private OrderDTO getOrderDTOForDateAndUser(String date, UserDiner user) {
		OrderDTO orderDTO = new OrderDTO();
		Menu menu = getMenuByDate(date);
		MenuDTO menuDTO = this.convertFromMenuToMenuDTO(menu);
		orderDTO.setMenuDTO(menuDTO);

		Order orderByDate = getOrderByUserAndDate(user, date);

		if(isOrderInDatabase(orderByDate)) {
			orderDTO.setOrderId(orderByDate.getId());
			
			Map<FoodDTO, Integer> quantitiesForOrder = getAllOrderedQuantitiesForOrder(getOrderByUserAndDate(user, date), menu);
//			Map<FoodDTO, Integer> sortedMap = quantitiesForOrder.entrySet().stream()
//					.sorted(Entry.comparingByValue(Comparator.reverseOrder()))
//					.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

//			orderDTO.setQuantities(sortedMap);
			orderDTO.setQuantities(quantitiesForOrder);

		}
		return orderDTO;
	}

	private List<FoodDTO> getAllFoodsForMenu(Menu menu) {
		List<Food> foods = new ArrayList<>();
		for (Dish dish : menu.getDishes()) {
			for (Food food : dish.getFoods()) {
				if(!foods.contains(food)) {
					foods.add(food);
				}
			}
		}
		return this.convertFromFoodsToFoodsDTO(foods);
	}
	

	private List<String> getAllOrderedDatesForUser(UserDiner user) {
		List<String> orderedDates = new ArrayList<>();
		for (Order order : this.getAllOrdersForUser(user)) {
			if((LocalDate.parse(order.getDate()).isAfter(LocalDate.now()) || LocalDate.parse(order.getDate()).isEqual(LocalDate.now()))  && !order.getTaken()) {
				orderedDates.add(order.getDate());
			}
		}
		return orderedDates;
	}

	@Transactional
	private void removeOrder(Integer orderId) {
		Order order = entityManager.find(Order.class, orderId);
		if(isOrderInDatabase(order)) {
			entityManager.remove(order);
		}
	}

	private boolean isOrderInDatabase(Order order) {
		for(Order orderObject : orderRepository.findAll()) {
			if(orderObject.equals(order)) {
				return true;
			}
		}
		return false;
	}

	private List<Order> getAllOrdersForUser(UserDiner user) {
		List<Order> ordersForUser = new ArrayList<>();

		for (Order order : orderRepository.findAll()) {
			if (order.getUserDiner().equals(user)) {
				ordersForUser.add(order);
			}
		}
		return ordersForUser;
	}

	private MenuDTO convertFromMenuToMenuDTO(Menu menu) {			
		MenuDTO menuDTO = new MenuDTO(menu.getId(), menu.getTitle(), menu.getDate(), menu.getState());
		return menuDTO;
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

	// gets an user id based on his name
	private Integer getUserIdByName(String name) {
		// iterates through all users
		for (UserDiner user : userRepository.findAll()) {
			// tests if there is an user with the given name
			if (user.getName().toUpperCase().trim().equals(name.toUpperCase().trim())) {
				// returns the id of that user
				return user.getId();
			}
		}
		return null;
	}

	private FoodDTO convertFromFoodToFoodDTO(Food food) {
		FoodDTO foodDTO = new FoodDTO();
		foodDTO.setFood(food);
		return foodDTO;
	}

	private List<Food> convertFromFoodsDTOToFoods(List<FoodDTO> foodsDTO) {
		List<Food> foods = new ArrayList<>();
		for (FoodDTO foodDTO : foodsDTO) {
			foods.add(foodDTO.getFood());
		}
		return foods;
	}
	
	private List<FoodDTO> convertFromFoodsToFoodsDTO(List<Food> foods) {
		List<FoodDTO> foodsDTO = new ArrayList<>();
		for (Food food : foods) {
			FoodDTO foodDTO = new FoodDTO();
			foodDTO.setFood(food);
			foodsDTO.add(foodDTO);
		}
		return foodsDTO;
	}

	private boolean testIfMapCointiansKey(Map<FoodDTO, Integer> map, FoodDTO key) {
		for (Map.Entry<FoodDTO, Integer> entry : map.entrySet()) {
			if (entry.getKey().getFood().equals(key.getFood())) {
				return true;
			}
		}
		return false;		
	}

	// e scrisa naspa, dar am facut-o sa mearga; 
	private Map<FoodDTO, Integer> getAllOrderedQuantitiesForOrder(Order order, Menu menu) {
		Map<FoodDTO, Integer> quantitiesMap = new LinkedHashMap<FoodDTO, Integer>();

		for (OrderQuantity orderQuantity : orderQuantityRepository.findAll()) {
			if (orderQuantity.getOrder().equals(order)) {
				quantitiesMap.put(convertFromFoodToFoodDTO(orderQuantity.getFoodd()), orderQuantity.getQuantity());
			}
		}
		Map<FoodDTO, Integer> quantitiesMap2 = new LinkedHashMap<FoodDTO, Integer>();

		for (FoodDTO foodDTO : this.getAllFoodsForMenu(menu)) {
			if (testIfMapCointiansKey(quantitiesMap, foodDTO)) {
				quantitiesMap2.put(foodDTO, getValueByKey(quantitiesMap, foodDTO));
			}
			else {
				quantitiesMap2.put(foodDTO, 0);
			}
		}
		return quantitiesMap2;
	}
	// am facut asta pt ca nu mergea cu metoda de get default
	private Integer getValueByKey(Map<FoodDTO, Integer> quantitiesMap, FoodDTO key) {
		for (Map.Entry<FoodDTO, Integer> entry : quantitiesMap.entrySet()) {
			if(entry.getKey().getFood().equals(key.getFood())) {
				return entry.getValue();
			}
		}
		return null;
	}
}
