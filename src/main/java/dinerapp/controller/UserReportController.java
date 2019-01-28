package dinerapp.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;

import dinerapp.model.UserReportViewModel;
import dinerapp.model.entity.Order;
import dinerapp.model.entity.OrderQuantity;
import dinerapp.model.entity.UserDiner;
import dinerapp.repository.OrderQuantityRepository;
import dinerapp.repository.OrderRepository;
import dinerapp.repository.UserCantinaRepository;

@Controller
public class UserReportController 
{
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserCantinaRepository userDinerRepository;
	
	@Autowired
	private OrderQuantityRepository orderQuantityRepository;
	
	private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@SessionScope
	@GetMapping("/nextWeekReportView")
	public String openNextWeekReportView(Model model) {
		System.out.println("Am intrat pe GET");
		System.out.println("Data de azi: " + getAllNextDate());
		
		UserReportViewModel userReportViewModel = new UserReportViewModel();
		userReportViewModel.setOrders(getAllOrderFromTable());
		userReportViewModel.setUsers(getAllUserDinerFromTable());
		userReportViewModel.setOrderQuantity(getAllOrderQuantityFromTable());
		//nextWeekViewModel.setDates(getAllNextDate());
		//
		List<String> date = new ArrayList<>();
		date.add("2019-01-27");
		userReportViewModel.setDates(date);
		model.addAttribute("nextWeekViewModel", userReportViewModel);

		return "views/nextWeekReportView";
	}
	
	@PostMapping("/nextWeekReportView")
	public String openNextWeekReportyyView(Model model, @ModelAttribute UserReportViewModel userReportViewModel,
														@RequestParam(value = "submit", required = false) String reqParam,
														@RequestParam(value = "dropdown_list") String reportDate) {

		System.out.println("Req param: " + reqParam);
		System.out.println("Data este: " + reportDate);
		List<String> date = new ArrayList<>();
		date.add("2019-01-27");
		userReportViewModel.setDates(date);
		//
		List<Order> listOfOrdersFromTable = getAllOrderFromTable();
		List<Order> todayOrder = new ArrayList<>();
		List<UserDiner> listOfUsersFromTable = getAllUserDinerFromTable();
		List<UserDiner> todayOrderUsers = new ArrayList<>();
		
		
		switch(reqParam) {
		case "Search": {
			System.out.println("Am intrat pe case-ul de Search!");
			System.out.println(reportDate);
			
			for (Order order : listOfOrdersFromTable) {
				if (order.getDate().equals(reportDate)) {
					todayOrder.add(order);
				}
			}
			
			break;
		}
		case "Submit": {
			System.out.println("Am intrat pe case-ul de Submit!");
			break;
		}
		}
		
		model.addAttribute("nextWeekViewModel", userReportViewModel);
		return "views/nextWeekReportView";
	}
	
	private List<Order> getAllOrderFromTable() { 
		Iterable<Order> list = orderRepository.findAll();
		List<Order> searchedList = new ArrayList<>();
		
		for (Order order : list) {
			searchedList.add(order);
		}
		
		return searchedList;
	}
	
	private List<UserDiner> getAllUserDinerFromTable() { 
		Iterable<UserDiner> list = userDinerRepository.findAll();
		List<UserDiner> searchedList = new ArrayList<>();
		
		for (UserDiner userDiner : list) {
			searchedList.add(userDiner);
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
	
	private List<String> getAllNextDate() {
		List<String> searchedDate = new ArrayList<>();
		String currentDate = sdf.format(new Date());
		
		for(int dayIndex = 0 ;dayIndex <= 4 ;dayIndex++) {
			searchedDate.add(incrementCurrentDayByIndex(currentDate, dayIndex));
		}
		
		return searchedDate;
	}
	
	private String incrementCurrentDayByIndex(String date, int index) {
		return LocalDate.parse(date).plusDays(index).toString();
	}
}
