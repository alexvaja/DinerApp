package dinerapp.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itextpdf.text.DocumentException;

import dinerapp.model.OrderViewModel;
import dinerapp.model.entity.Food;
import dinerapp.model.entity.Order;
import dinerapp.model.entity.OrderQuantity;
import dinerapp.pdf.ExportToPDF;
import dinerapp.repository.FoodRepository;
import dinerapp.repository.OrderQuantityRepository;
import dinerapp.repository.OrderRepository;
import dinerapp.security.utils.OrderComparer;

@Controller
public class NextDayReportController {

	@Autowired
	private OrderRepository orderRepo;
	@Autowired
	private FoodRepository foodRepo;
	@Autowired
	private OrderQuantityRepository orderQuantityRepo;

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

	private List<OrderQuantity> geListOfOrderQuantity() {
		final Iterable<OrderQuantity> list = orderQuantityRepo.findAll();
		final List<OrderQuantity> orderQuantityList = new ArrayList<>();
		for (final OrderQuantity orderQuantity : list) {
			orderQuantityList.add(orderQuantity);
		}
		return orderQuantityList;
	}

	private void sortOrderList(List<Order> orders) {
		Collections.sort(orders, new OrderComparer());
	}

	public void retrieveData(List<OrderQuantity> orderQuantity, List<Food> foods, String reportDate,
			List<Integer> quantities) {
		for (OrderQuantity OQ : orderQuantity) {
			for (int index = 0; index < foods.size(); index++) {
				if (OQ.getFoodd().equals(foods.get(index)) && OQ.getOrder().getDate().equals(reportDate)) {
					int quantity = quantities.get(index);
					System.out.println(OQ.getQuantity());
					quantity += OQ.getQuantity().intValue();
					quantities.set(index, quantity);
				}
			}
		}
	}
	
	@GetMapping("/nextDayReportView")
	public String getMap(Model model) {
		OrderViewModel orderViewModel = new OrderViewModel();
		model.addAttribute("orderViewModel", orderViewModel);
		return "views/nextDayReportView";
	}

	@PostMapping("/nextDayReportView")
	public String postMap(Model model, @ModelAttribute OrderViewModel orderViewModel,
			@RequestParam(value = "submit") String reqParam,
			@RequestParam(value = "report_date", required = true) 
		    String reportDate, HttpServletResponse response) 
	{
		orderViewModel.setDate(reportDate);
		List<Order> orders = getListOfOrders();
		List<Food> foods = getListOfFoods();
		List<OrderQuantity> orderQuantity = geListOfOrderQuantity();
		List<Integer> quantities = new ArrayList<>();
		List<OrderQuantity> requestedDateOrderQuantity = new ArrayList<>();
	
		sortOrderList(orders);
		orderViewModel.setOrders(orders);
	
		for (OrderQuantity oq : orderQuantity)
			if (oq.getOrder().getDate().equals(reportDate))
				requestedDateOrderQuantity.add(oq);
	
		for (int i = 0; i < foods.size(); i++)
			quantities.add(i, 0);
	
		switch (reqParam) 
		{
			case "submit": 
			{
				retrieveData(orderQuantity, foods, reportDate, quantities);
				orderViewModel.setFoods(foods);
				orderViewModel.setQuantities(quantities);
				model.addAttribute("orderViewModel", orderViewModel);
				// ExportToPDF.exportToPDF("export.pdf", foods, quantities);
				return "views/nextDayReportView";
			}
			case "export": 
			{
	
				// ExportToPDF.downloadFile(response, "output/raport.pdf");
				retrieveData(orderQuantity, foods, reportDate, quantities);
	
				orderViewModel.setFoods(foods);
				orderViewModel.setQuantities(quantities);
				System.out.println("Lista food: " + foods);
				System.out.println("Lista cantitati: " + quantities);
	
				model.addAttribute("orderViewModel", orderViewModel);
				try 
				{
					ExportToPDF.exportToPDF("Raport.pdf", foods, quantities, reportDate);
				} 
				catch (FileNotFoundException | DocumentException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				return "views/nextDayReportView";
			}
			case "download":
			{
				try
				{
					System.out.println("AM INTRAT IN 1!");
					ExportToPDF.downloadFile(response, "Raport.pdf");
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				return "views/nextDayReportView";
			}
			default:
				return "views/nextDayReportView";
		}
	}
}
