package dinerapp.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
	public String openNextWeekReportView(Model model, HttpSession session) {
		System.out.println("Am intrat pe GET");
		
		UserReportViewModel userReportViewModel = new UserReportViewModel();
		userReportViewModel.setDates(getAllNextDate());

		session.setAttribute("userReportViewModel", userReportViewModel);

		return "views/userReportView";
	}

	@PostMapping("/userReportView")
	public String openNextWeekReportyyView(Model model,
			@SessionAttribute("userReportViewModel") UserReportViewModel userReportViewModel,
			@RequestParam(value = "submit", required = false) String reqParam,
			@RequestParam(value = "dropdown_list", required = false) String reportDate,
			@RequestParam(value = "checkbox_list", required = false) String selectedUsers) {
		System.out.println("Am intrat pe POST");
		userReportViewModel.setDates(getAllNextDate());

		List<Order> listOfOrdersFromTable = getAllOrderFromTable();
		List<OrderQuantity> listOfOrdersQuantitiesFromTable = getAllOrderQuantityFromTable();

		List<Order> todayOrders = new ArrayList<>();
		List<OrderQuantity> todayOrdersQuantities = new ArrayList<>();

		switch (reqParam) {
		case "Search": {
			System.out.println("Am intrat pe case-ul de Search!");

			for (Order order : listOfOrdersFromTable) {
				if (order.getDate().equals(reportDate) && order.getTaken().equals(false)) {
					todayOrders.add(order);
				}
			}

			for (OrderQuantity orderQuantity : listOfOrdersQuantitiesFromTable) {
				for (Order order : todayOrders) {
					if (orderQuantity.getOrder().equals(order)) {
						todayOrdersQuantities.add(orderQuantity);
					}
				}
			}

			List<String> magicList = new ArrayList<>();

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

			List<OrderDTO> ordersDTO = new ArrayList<>();
			for (int index = 0; index < todayOrders.size(); index++) {
				OrderDTO orderDTO = new OrderDTO();
				orderDTO.setOrder(todayOrders.get(index));
				orderDTO.setToBeDeliveredFood(magicList.get(index));
				ordersDTO.add(orderDTO);
			}
			System.out.println("Lista today: " + todayOrders);
			userReportViewModel.setListOfFoods(ordersDTO);
			break;
		}
		case "Submit": {
			System.out.println("Am intrat pe case-ul de Submit!");

			for (Order order : listOfOrdersFromTable) {
				if (order.getDate().equals(reportDate)) {
					todayOrders.add(order);
				}
			}

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

	private List<Order> getAllOrderFromTable() {
		Iterable<Order> list = orderRepository.findAll();
		List<Order> searchedList = new ArrayList<>();

		for (Order order : list) {
			searchedList.add(order);
		}

		return searchedList;
	}

//	private List<UserDiner> getAllUserDinerFromTable() { 
//		Iterable<UserDiner> list = userDinerRepository.findAll();
//		List<UserDiner> searchedList = new ArrayList<>();
//		
//		for (UserDiner userDiner : list) {
//			searchedList.add(userDiner);
//		}
//		
//		return searchedList;
//	}	

	private List<OrderQuantity> getAllOrderQuantityFromTable() {
		Iterable<OrderQuantity> list = orderQuantityRepository.findAll();
		List<OrderQuantity> searchedList = new ArrayList<>();

		for (OrderQuantity orderQuantity : list) {
			searchedList.add(orderQuantity);
		}

		return searchedList;
	}

	private List<String> getAllNextDate() {
		List<String> searchedDate = new ArrayList<>();
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		System.out.println("Calendar: " + calendar);
		System.out.println("DATA: " + date);
		String currentDate = sdf.format(new Date());
		
		

		for (int dayIndex = 0; dayIndex <= 4; dayIndex++) {
			searchedDate.add(incrementCurrentDayByIndex(currentDate, dayIndex));
		}

		return searchedDate;
	}

	private String incrementCurrentDayByIndex(String date, int index) {
		return LocalDate.parse(date).plusDays(index).toString();
	}

//	private List<Order> getAllOrdersForGivenDate(String date) {
//		return null;
//	}
}
