package dinerapp.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.annotation.SessionScope;

import dinerapp.exceptions.InternalServerException;
import dinerapp.exceptions.NewSessionException;
import dinerapp.model.UserReportViewModel;
import dinerapp.model.dto.OrderDTO;
import dinerapp.model.entity.Order;
import dinerapp.model.entity.OrderQuantity;
import dinerapp.repository.OrderQuantityRepository;
import dinerapp.repository.OrderRepository;

@Controller
public class UserReportController {
	
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderQuantityRepository orderQuantityRepository;

	private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@ExceptionHandler({ NewSessionException.class })
	public String sessionError() {
		LOGGER.error("incercare de acces nepermis");
        //Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Request: " + request.getRequestURL() + " raised " + e);
		return "views/loginView";
	}
	
	@ExceptionHandler({ InternalServerException.class })
	public String sessionServerError() {
		LOGGER.error("internal server status");
        //Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Request: " + request.getRequestURL() + " raised " + e);
		return "views/loginView";
	}


	@SessionScope
	@GetMapping("/userReportView")
	public String getController(HttpSession session, Model model, 
													  HttpServletRequest httpRequest) throws ParseException, NewSessionException, InternalServerException {

		if (session.isNew()) {
			throw new NewSessionException();			
		}
		
		LOGGER.info("Am intrat pe GET");
		
		UserReportViewModel userReportViewModel = new UserReportViewModel();
		userReportViewModel.setDate(getTodayDate());
		
		List<OrderDTO> ordersDTO = new ArrayList<>(); 
		ordersDTO.addAll(getListOrdersDTOForDate(userReportViewModel.getDate()));
		
		Collections.sort(ordersDTO, new Comparator<OrderDTO>(){
			   @Override
			   public int compare(OrderDTO leftOrder, OrderDTO rightOrder) {
				   //System.out.println(leftOrder.getOrder().getUserDiner().getName().compareTo(rightOrder.getOrder().getUserDiner().getName()));
				   return leftOrder.getOrder().getUserDiner().getName().compareTo(rightOrder.getOrder().getUserDiner().getName());				   
			   }
			 });
		
		userReportViewModel.setListOfFoods(ordersDTO);

		session.setAttribute("userReportViewModel", userReportViewModel);

		return "views/userReportView";
	}

	@PostMapping("/userReportView")
	public String postController(Model model, HttpStatus status, @SessionAttribute("userReportViewModel") UserReportViewModel userReportViewModel,
														@RequestParam(value = "submit", required = false) String reqParam,
														@RequestParam(value = "checkbox_list", required = false) String selectedUsers) throws ParseException, InternalServerException {
		LOGGER.info("Am intrat pe POST");
		
		switch (reqParam) {
		case "Trimite": {
			
			if (selectedUsers != null) {

				String[] checkedOrderList = selectedUsers.split(",");
				
				for (int index = 0; index < checkedOrderList.length; index++) {
					for (int indexFood = 0; indexFood < userReportViewModel.getListOfFoods().size(); indexFood++) {
						Order order = userReportViewModel.getListOfFoods().get(indexFood).getOrder();
						if (Integer.parseInt(checkedOrderList[index]) == order.getId()) {
							order.setTaken(true);
							orderRepository.save(order);
						}
					}
				}							
					
				List<OrderDTO> ordersDTO = new ArrayList<>(); 
				ordersDTO.addAll(getListOrdersDTOForDate(userReportViewModel.getDate()));
				
				Collections.sort(ordersDTO, new Comparator<OrderDTO>(){
					   @Override
					   public int compare(OrderDTO leftOrder, OrderDTO rightOrder) {
						  // System.out.println(leftOrder.getOrder().getUserDiner().getName().compareTo(rightOrder.getOrder().getUserDiner().getName()));
						   return leftOrder.getOrder().getUserDiner().getName().compareTo(rightOrder.getOrder().getUserDiner().getName());				   
					   }
					 });
				
				userReportViewModel.setListOfFoods(ordersDTO);
				
				//userReportViewModel.setListOfFoods(getListOrdersDTOForDate(userReportViewModel.getDate()));
			}
			
			break;
		}
		}

		return "views/userReportView";
	}
	
	private List<Order> getAllOrdersToBeDeliveredForDate(String date) {
		
		List<Order> listOfOrdersFromTable = getAllOrderFromTable();
		List<Order> listOfOrdersToBeDelivered = new ArrayList<>();
		
		for (Order order : listOfOrdersFromTable) {
			if (order.getDate().equals(date) && order.getTaken().equals(false)) {
				listOfOrdersToBeDelivered.add(order);
			}
		}
		return listOfOrdersToBeDelivered;
	}
	
	private List<OrderQuantity> getAllOrderQuantityToBeDelivered(List<Order> orders) {
		
		List<OrderQuantity> listOfOrdersQuantitiesFromTable = getAllOrderQuantityFromTable();
		List<OrderQuantity> listOfOrdersQuantitiesToBeDelivered = new ArrayList<>();

			for (OrderQuantity orderQuantity : listOfOrdersQuantitiesFromTable) {
				for (Order order : orders) {
					if (orderQuantity.getOrder().equals(order)) {
						listOfOrdersQuantitiesToBeDelivered.add(orderQuantity);
					}
				}
			}
		return listOfOrdersQuantitiesToBeDelivered;
	}
	
	private List<String> toBeDeliveredListForDate(String date) {
		
		List<String> magicList = new ArrayList<>();
		List<Order> todayOrders = getAllOrdersToBeDeliveredForDate(date);
		List<OrderQuantity> todayOrdersQuantities = getAllOrderQuantityToBeDelivered(todayOrders);
		
		for (Order order : todayOrders) {
			String foodString = new String();
			for (OrderQuantity orderQuantity : todayOrdersQuantities) {
				if (order.equals(orderQuantity.getOrder())) {
					foodString += (orderQuantity.getFoodd().getName() + " X"
							+ orderQuantity.getQuantity().toString() + "  ");
				}
			}
			magicList.add(foodString);
		}
		
		return magicList;
	}
	
	private List<OrderDTO> getListOrdersDTOForDate(String date){
		
		List<Order> todayOrders = getAllOrdersToBeDeliveredForDate(date);
		List<String> magicList = toBeDeliveredListForDate(date);
		List<OrderDTO> ordersDTO = new ArrayList<>(); //face modelul pt sesiune
		
		for (int index = 0; index < todayOrders.size(); index++) {
			
			OrderDTO orderDTO = new OrderDTO();
			orderDTO.setOrder(todayOrders.get(index));
			orderDTO.setToBeDeliveredFood(magicList.get(index));
			ordersDTO.add(orderDTO);
		}
		
		return ordersDTO;
	}

	private List<Order> getAllOrderFromTable() {
		Iterable<Order> list = orderRepository.findAll();
		List<Order> searchedList = new ArrayList<>();

		for (Order order : list) {
			searchedList.add(order);
		}

		return searchedList;
	}

	private List<OrderQuantity> getAllOrderQuantityFromTable() {
		Iterable<OrderQuantity> list = orderQuantityRepository.findAll();
		List<OrderQuantity> searchedList = new ArrayList<>();

		for (OrderQuantity orderQuantity : list) {
			searchedList.add(orderQuantity);
		}

		return searchedList;
	}
	
	private String getTodayDate() {
		Calendar calendar = Calendar.getInstance();
		return sdf.format(calendar.getTime());
	}

//	private void updateDB() {
//		
//		List<Order> order = getAllOrderFromTable();
//		for (Order o : order) {
//			Order newO = o;
//			newO.setTaken(false);
//			newO.setDate(getTodayDate());
//			orderRepository.save(newO);
//		}
//	}	

}
