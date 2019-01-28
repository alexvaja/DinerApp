package dinerapp.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
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
	@GetMapping("/userReportView")
	public String openNextWeekReportView(Model model, HttpSession session) {
		System.out.println("Am intrat pe GET");
		
		UserReportViewModel userReportViewModel = new UserReportViewModel();
		userReportViewModel.setDates(getAllNextDate());

		session.setAttribute("userReportViewModel", userReportViewModel);

		return "views/userReportView";
	}
	
	@PostMapping("/userReportView")
	public String openNextWeekReportyyView(Model model, @SessionAttribute("userReportViewModel") UserReportViewModel userReportViewModel,
														@RequestParam(value = "submit", required = false) String reqParam,
														@RequestParam(value = "dropdown_list", required=false) String reportDate,
														@RequestParam(value = "checkbox_list", required=false) String selectedUsers) {
		System.out.println("Am intrat pe POST");
		System.out.println("Req param: " + reqParam);
		System.out.println("Data este: " + reportDate);
		userReportViewModel.setDates(getAllNextDate());

		List<Order> listOfOrdersFromTable = getAllOrderFromTable();
		List<OrderQuantity> listOfOrdersQuantities = getAllOrderQuantityFromTable();
		
		List<Order> todayOrder = new ArrayList<>();
		
		
		switch(reqParam) {
		case "Search": {
			System.out.println("Am intrat pe case-ul de Search!");
			System.out.println(reportDate);
			
			for (Order order : listOfOrdersFromTable) {
				if (order.getDate().equals(reportDate)) {
					todayOrder.add(order);
				}
			}
			
			System.out.println("todayList: " + todayOrder);


			userReportViewModel.setOrders(todayOrder);
			
			break;
		}
		case "Submit": {
			System.out.println("Am intrat pe case-ul de Submit!");
			System.out.println("CheckList: " + selectedUsers);
			
			String[] selectedUsersList = selectedUsers.split(",");
			
			for (Order order : todayOrder) {
				for (int index = 0; index < selectedUsersList.length; index++) {
					if (order.getId().equals(selectedUsersList[index])) {
						Order o = order;
						System.out.println(o);
						o.setTaken(true);
						System.out.println(o);
						orderRepository.save(o);
					}
				}
				
			}

			break;
		}
		}
		
		//model.addAttribute("userReportViewMode", userReportViewModel);
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
	
	private List<Order> getAllOrdersForGivenDate(String date) {
		return null;
	}
}


//Order order = todayOrder.get(0);
//order.setTaken(false);

//Order order1 = new Order();
//order1.setDate("2019-01-28");
//order1.setTaken(false);
//UserDiner userDiner = new UserDiner();
//userDiner.setId(3);
//order1.setUserDiner(userDiner);
//
//orderRepository.save(order1);

//Order order1 = new Order();
//order1.setDate("2019-01-28");
//order1.setTaken(false);
//UserDiner userDiner = new UserDiner();
//userDiner.setId(4);
//order1.setUserDiner(userDiner);
//
//orderRepository.save(order1);


//if(selectedUsers != null ) {
//	String[] selectedUsersList = selectedUsers.split(",");
//	
//	for (Order order : todayOrder) {
//		for (int index = 0; index < selectedUsersList.length; index++) {
//			if (order.getId().equals(selectedUsersList[index])) {
//				Order o = order;
//				System.out.println(o);
//				o.setTaken(true);
//				System.out.println(o);
//				orderRepository.save(o);
//			}
//		}
//		
//	}
//}