package dinerapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import dinerapp.model.OrderViewModel;
import dinerapp.model.entity.Food;
import dinerapp.model.entity.Order;
import dinerapp.repository.FoodRepository;
import dinerapp.repository.OrderRepository;
import dinerapp.security.utils.OrderComparer;

@Controller
public class NextDayReportController {
	@Autowired
	private OrderRepository orderRepo;
	@Autowired
	private FoodRepository foodRepo;

	private List<Order> getListOfOrders() {
		final Iterable<Order> list = orderRepo.findAll();
		final List<Order> orderList = new ArrayList<>();
		for (final Order order : list) {
			orderList.add(order);
		}
		return orderList;
	}

	private List<Food> getListOfFoods() {
		final Iterable<Food> list = foodRepo.findAll();
		final List<Food> foodList = new ArrayList<>();
		for (final Food order : list) {
			foodList.add(order);
		}
		return foodList;
	}

	private void sortOrderList(List<Order> orders) {
		Collections.sort(orders, new OrderComparer());
	}

	@GetMapping("/nextDayReportView")
	public String sessionExample(Model model) 
	{
//		System.out.println("Am intrat in pula lu galmeanu");
//		OrderViewModel orderViewModel = new OrderViewModel();
//		List<Order> orders = new ArrayList<>();
//		List<Food> foods = new ArrayList<>();
//		foods.addAll(getListOfFoods());
//		orders.addAll(getListOfOrders());
//		sortOrderList(orders);
//		orderViewModel.setOrders(orders);
//		
//		List<Integer> quantities = new ArrayList<>(foods.size());
//		for(int i = 0 ; i < foods.size(); i++)
//			quantities.add(i,0);
//		
//		for(Order order : orders)
//		{
//			for(int index = 0; index < foods.size(); index++)
//			{
//				if(order.get().equals(foods.get(index)))
//				{
//					int quantity = quantities.get(index);
//					quantity++;
//					quantities.set(index, quantity);
//				}
//			}
//		}
//		orderViewModel.setFoods(foods);
//		orderViewModel.setQuantities(quantities);
//		model.addAttribute("orderViewModel", orderViewModel);
		return "views/nextDayReportView";
	}
	
	@PostMapping("/nextDayReportView")
	public String postNextDayReport(Model model)
	{
		return "views/nextDayReportView"; 
	}
}
