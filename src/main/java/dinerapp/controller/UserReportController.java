package dinerapp.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

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

	@SessionScope
	@GetMapping("/userReportView")
	public String openNextWeekReportView(Model model, HttpSession session) throws ParseException {
		LOGGER.info("Am intrat pe GET");
		System.out.println("Am intrat pe GET");
		//updateDB();
		
		UserReportViewModel userReportViewModel = new UserReportViewModel();
		userReportViewModel.setDates(getAllNextDate());

		session.setAttribute("userReportViewModel", userReportViewModel);

		return "views/userReportView";
	}

	@PostMapping("/userReportView")
	public String openNextWeekReportyyView(Model model, @SessionAttribute("userReportViewModel") UserReportViewModel userReportViewModel,
														@RequestParam(value = "submit", required = false) String reqParam,
														@RequestParam(value = "dropdown_list", required = false) String reportDate,
														@RequestParam(value = "checkbox_list", required = false) String selectedUsers) throws ParseException {
		
		System.out.println("Am intrat pe POST");
		userReportViewModel.setDates(getAllNextDate());

		switch (reqParam) {
		case "Search": {
			System.out.println("Am intrat pe case-ul de Search!");

			List<Order> todayOrders = getAllOrdersToBeDeliveredForDate(reportDate);
			//List<OrderQuantity> todayOrdersQuantities = getAllOrderQuantityToBeDelivered(todayOrders);//in plus
			List<String> magicList = toBeDeliveredListForDate(reportDate);
			List<OrderDTO> ordersDTO = new ArrayList<>(); //face modelul pt sesiune
			
			for (int index = 0; index < todayOrders.size(); index++) {
				
				OrderDTO orderDTO = new OrderDTO();
				orderDTO.setOrder(todayOrders.get(index));
				orderDTO.setToBeDeliveredFood(magicList.get(index));
				ordersDTO.add(orderDTO);
			}
			
			userReportViewModel.setListOfFoods(ordersDTO);
			
			break;
		}
		case "Submit": {
			System.out.println("Am intrat pe case-ul de Submit!");
			
			List<Order> todayOrders = getAllOrdersToBeDeliveredForDate(reportDate);
			
			String[] checkedOrderList = selectedUsers.split(",");

			for (int index = 0; index < checkedOrderList.length; index++) {
				for (Order order : todayOrders) {
					Order newOrder = order;
					newOrder.setTaken(true);

					if (Integer.parseInt(checkedOrderList[index]) == order.getId()) {
						orderRepository.save(newOrder);
					}
				}
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
	
	private List<String> toBeDeliveredListForDate(String date) {//modifica nume magicList
		
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
	
	

	private List<Order> getAllOrderFromTable() {
		Iterable<Order> list = orderRepository.findAll();
		List<Order> searchedList = new ArrayList<>();

		for (Order order : list) {
			searchedList.add(order);
		}

		return searchedList;
	}
	
	private void updateDB() {
		
		List<Order> order = getAllOrderFromTable();
		for (Order o : order) {
			Order newO = o;
			newO.setTaken(false);
			orderRepository.save(newO);
		}
	}	

	private List<OrderQuantity> getAllOrderQuantityFromTable() {
		Iterable<OrderQuantity> list = orderQuantityRepository.findAll();
		List<OrderQuantity> searchedList = new ArrayList<>();

		for (OrderQuantity orderQuantity : list) {
			searchedList.add(orderQuantity);
		}

		return searchedList;
	}

	private List<String> getAllNextDate() throws ParseException {

		List<String> searchedDate = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		String currentDate = sdf.format(calendar.getTime());
		int dayIndex = 0;
				
		while (searchedDate.size() < 5) {

			calendar.setTime(sdf.parse(incrementCurrentDayByIndex(currentDate, dayIndex)));
			if (calendar.get(Calendar.DAY_OF_WEEK) != 7 && calendar.get(Calendar.DAY_OF_WEEK) != 1) {
				searchedDate.add(incrementCurrentDayByIndex(currentDate, dayIndex));
			}
			dayIndex++;
		}
		
		return searchedDate;
	}

	private String incrementCurrentDayByIndex(String date, int index) {
		return LocalDate.parse(date).plusDays(index).toString();
	}
}
