package dinerapp.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.annotation.SessionScope;

import com.itextpdf.text.DocumentException;

import dinerapp.exceptions.NewSessionException;
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
public class NextDayReportController 
{
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private FoodRepository foodRepo;
	
	@Autowired
	private OrderQuantityRepository orderQuantityRepo;
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@ExceptionHandler({ NewSessionException.class })
	public String sessionError()
	{
		LOGGER.error("incercare de acces nepermis");
		return "views/loginView";
	}
	//retrieve in a list all foods from data base
	private List<Food> getListOfFoods() 
	{
		final Iterable<Food> list = foodRepo.findAll();
		final List<Food> foodList = new ArrayList<>();
		for (final Food order : list) 
		{
			foodList.add(order);
		}
		return foodList;
	}
	//retrieve in a list all OrderQuantity from data base
	private List<OrderQuantity> geListOfOrderQuantity() 
	{
		final Iterable<OrderQuantity> list = orderQuantityRepo.findAll();
		final List<OrderQuantity> orderQuantityList = new ArrayList<>();
		for (final OrderQuantity orderQuantity : list) 
		{
			orderQuantityList.add(orderQuantity);
		}
		return orderQuantityList;
	}
	
	//dividing the orders by time in 2 lists
	private void divideTheOrdersByTime(List<Order> ordersBefore4PM, List<Order>ordersAfter4PM)
	{
		List<Order> allOrders = getListOfOrders();
		for(Order order: allOrders)
		{
			if(order.getHour() != null)
			{
				if(order.getHour().equals("00:00"))
					ordersBefore4PM.add(order);
				else
					ordersAfter4PM.add(order);
			}
		}
		for(Order order : ordersBefore4PM)
		{
			System.out.println(order.toString());
		}
		for(Order order : ordersAfter4PM)
		{
			System.out.println(order.toString());
		}
	}
	
	//dividing the OrderQuantities by time in 2 lists
	private void divideTheOrderQuantitiesByTime(List<OrderQuantity>orderQuantityBefore4PM, 
			List<OrderQuantity> orderQuantityAfter4PM, List<Order> after, List<Order>before, String reportDate)
	{
		List<OrderQuantity> allOQ = geListOfOrderQuantity();
		for(Order order : after)
		{
			for(int index = 0; index < allOQ.size(); index++)
				if(order.getId().equals(allOQ.get(index).getOrder().getId()) && order.getDate().equals(reportDate))
				{
					orderQuantityAfter4PM.add(allOQ.get(index));
				}
		}
		for(Order order : before)
		{
			for(int index = 0; index < allOQ.size(); index++)
				if(order.getId().equals(allOQ.get(index).getOrder().getId()) && order.getDate().equals(reportDate))
				{
					orderQuantityBefore4PM.add(allOQ.get(index));
				}
		}
		for(OrderQuantity OQ : orderQuantityBefore4PM)
		{
			System.out.println(OQ.toString());
		}
		for(OrderQuantity OQ : orderQuantityAfter4PM)
		{
			System.out.println(OQ.toString());
		}
	}

	private void sortOrderList(List<Order> orders)
	{
		Collections.sort(orders, new OrderComparer());
	}

	public void retrieveData(List<OrderQuantity> orderQuantity, List<Food> foods, String reportDate,List<Integer> quantities) 
	{
		for (OrderQuantity OQ : orderQuantity) 
		{
			for (int index = 0; index < foods.size(); index++) 
			{
				if (OQ.getFoodd().equals(foods.get(index)) && OQ.getOrder().getDate().equals(reportDate)) 
				{
					int quantity = quantities.get(index);
					quantity += OQ.getQuantity().intValue();
					quantities.set(index, quantity);
				}
			}
		}
	}
	
	public void retrieveDataForDisplayBefore4PM(List<OrderQuantity>before, List<Food> foods, 
			List<Integer> quantitiesBefore4PM, String reportDate)
	{
		for(OrderQuantity OQ : before)
		{
			for(int index = 0; index< foods.size(); index++)
			{
				if(OQ.getFoodd().equals(foods.get(index)) && OQ.getOrder().getDate().equals(reportDate))
				{
					int quantity = quantitiesBefore4PM.get(index);
					quantity += OQ.getQuantity().intValue();
					quantitiesBefore4PM.set(index, quantity);
				}
			}
		}
	}
	public void retrieveDataForDisplayAfter4PM(List<OrderQuantity>after, List<Food> foods, 
			List<Integer> quantitiesAfter4PM, String reportDate)
	{
		for(OrderQuantity OQ : after)
		{
			for(int index = 0; index < foods.size(); index++)
			{
				if(OQ.getFoodd().equals(foods.get(index)) && OQ.getOrder().getDate().equals(reportDate))
				{
					int quantity = quantitiesAfter4PM.get(index);
					quantity += OQ.getQuantity().intValue();
					quantitiesAfter4PM.set(index, quantity);
				}
			}
		}
	}
	
	@SessionScope
	@GetMapping("/reportView")
	public String getMap(Model model, HttpSession session) throws NewSessionException
	{
		if (session.isNew()) 
		{
			throw new NewSessionException();			
		}
		
		OrderViewModel orderViewModel = new OrderViewModel();
		//model.addAttribute("orderViewModel", orderViewModel);
		session.setAttribute("orderViewModel", orderViewModel);
		return "views/reportView";
	}

	@PostMapping("/reportView")
	public String postMap(Model model, @SessionAttribute("orderViewModel") OrderViewModel orderViewModel, HttpSession session, 
			HttpServletResponse response,
			@RequestParam(value = "submit") String reqParam,
			@RequestParam(value = "report_date", required = true) String reportDate) 
	{		
		orderViewModel.setDate(reportDate);
		//List<Order> orders = getListOfOrders();
		List<Food> foods = getListOfFoods();
		
		List<Integer> quantityBefore4PM = new ArrayList<>();
		List<Integer> quantityAfter4PM = new ArrayList<>();

		//Imi fac 2 liste de Order pentru comenzile inainte si dupa ora 4
		List<Order> ordersAfter4PM = new ArrayList<>();
		List<Order> ordersBefore4PM = new ArrayList<>();		
		divideTheOrdersByTime(ordersBefore4PM, ordersAfter4PM);

		//sortez listele de omenzi
		sortOrderList(ordersBefore4PM);
		sortOrderList(ordersBefore4PM);
		//pun comenzile sortate in view model 
		orderViewModel.setOrdersAfter4PM(ordersAfter4PM);
		orderViewModel.setOrdersBefore4PM(ordersBefore4PM);
		
		//Imi fac 2 liste de OrderQuantity pentru comenzile inainte si dupa ora 4
		List<OrderQuantity> requestedDateOrderQuantityAfter4PM = new ArrayList<>();
		List<OrderQuantity> requestedDateOrderQuantityBefore4PM = new ArrayList<>();
		divideTheOrderQuantitiesByTime(requestedDateOrderQuantityBefore4PM, requestedDateOrderQuantityAfter4PM, 
				ordersAfter4PM, ordersBefore4PM, reportDate);

		for (int i = 0; i < foods.size(); i++)
		{
			quantityAfter4PM.add(i,0);
			quantityBefore4PM.add(i, 0);				
		}
	
		switch (reqParam) 
		{
			case "Trimite": 
			{

				retrieveDataForDisplayBefore4PM(requestedDateOrderQuantityBefore4PM, foods, quantityBefore4PM,reportDate);
				retrieveDataForDisplayAfter4PM(requestedDateOrderQuantityAfter4PM, foods, quantityAfter4PM, reportDate);
				orderViewModel.setFoods(foods);
				orderViewModel.setAfter4PMQuantities(quantityAfter4PM);
				orderViewModel.setBefore4PMQuantities(quantityBefore4PM);
				
				model.addAttribute("orderViewModel", orderViewModel);
				return "views/reportView";
			}
			case "Descarca": 
			{
				String pdfFileName = reportDate + " Raport.pdf";

				retrieveDataForDisplayBefore4PM(requestedDateOrderQuantityBefore4PM, foods, quantityBefore4PM,reportDate);
				retrieveDataForDisplayAfter4PM(requestedDateOrderQuantityAfter4PM, foods, quantityAfter4PM, reportDate);				
				
				orderViewModel.setFoods(foods);
				orderViewModel.setAfter4PMQuantities(quantityAfter4PM);
				orderViewModel.setBefore4PMQuantities(quantityBefore4PM);
				model.addAttribute("orderViewModel", orderViewModel);
				try 
				{
					ExportToPDF.exportToPDF(pdfFileName,foods, quantityBefore4PM, quantityAfter4PM, reportDate);
					ExportToPDF.downloadFile(response, pdfFileName);
				} 
				catch (FileNotFoundException | DocumentException e) 
				{
					e.printStackTrace();
				} 
				catch(IOException e)
				{
					e.printStackTrace();
				}
				break;
			}
		}
		session.setAttribute("orderViewModel", orderViewModel);
		return "views/reportView";
	}
	private List<Order> getListOfOrders() 
	{
		final Iterable<Order> list = orderRepo.findAll();
		final List<Order> orderList = new ArrayList<>();
		for (final Order order : list)
		{
			orderList.add(order);
		}
		return orderList;
	}
}
