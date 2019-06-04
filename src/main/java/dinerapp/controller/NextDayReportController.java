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
	
	@SuppressWarnings("unused")
	private void divideQuantitiesByTime(List<Integer>quantitiesBefore4PM, List<Integer>quantitiesAfter4PM,
			List<Order>ordersBefore4PM, List<Order>ordersAfter4PM, List<Food> foods, String reportDate)
	{
		List<OrderQuantity> orderQuantities = geListOfOrderQuantity();  
		for(OrderQuantity OQ : orderQuantities)
		{
			for(int index = 0; index < foods.size(); index++)
			{
				if(OQ.getFoodd().equals(foods.get(index)) && OQ.getOrder().getDate().equals(reportDate))
				{
					
				}
			}
		}	
	}
	//dividing the orders by time in 2 lists
	private void divideTheOrdersByTime(List<Order> ordersBefore4PM, List<Order>ordersAfter4PM)
	{
		List<Order> allOrders = getListOfOrders();
		for(Order order: allOrders)
		{
			if(order.getHour().equals("00:00"))
				ordersBefore4PM.add(order);
			else
				ordersAfter4PM.add(order);
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
	
	public void divideQuantitiesByTime()
	{
		
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
		List<OrderQuantity> orderQuantity = geListOfOrderQuantity();
		List<Integer> quantities = new ArrayList<>();		
		List<OrderQuantity> requestedDateOrderQuantity = new ArrayList<>();

		//Imi fac 2 liste de OrderQuantity pentru comenzile inainte si dupa ora 4
		List<OrderQuantity> requestedDateOrderQuantityAfter4PM = new ArrayList<>();
		List<OrderQuantity> requestedDateOrderQuantityBefore4PM = new ArrayList<>();
		divideTheOrderQuantitiesByTime(requestedDateOrderQuantityAfter4PM, requestedDateOrderQuantityBefore4PM, 
				getListOfOrders(), reportDate);
		
		//Imi fac 2 liste de Order pentru comenzile inainte si dupa ora 4
		List<Order> ordersAfter4PM = new ArrayList<>();
		List<Order> ordersBefore4PM = new ArrayList<>();		
		divideTheOrdersByTime(ordersBefore4PM, ordersAfter4PM);
		
		sortOrderList(ordersBefore4PM);
		sortOrderList(ordersBefore4PM);		
		//sortOrderList(orders);
		
		//orderViewModel.setOrders(orders);
		orderViewModel.setOrdersAfter4PM(ordersAfter4PM);
		orderViewModel.setOrdersBefore4PM(ordersBefore4PM);
		
		for (OrderQuantity oq : orderQuantity)
			if (oq.getOrder().getDate().equals(reportDate))
				requestedDateOrderQuantity.add(oq);
	
		for (int i = 0; i < foods.size(); i++)
			quantities.add(i, 0);
	
		switch (reqParam) 
		{
			case "Trimite": 
			{
				retrieveData(orderQuantity, foods, reportDate, quantities);
				orderViewModel.setFoods(foods);
				orderViewModel.setQuantities(quantities);
				model.addAttribute("orderViewModel", orderViewModel);
				return "views/reportView";
			}
			case "Descarca": 
			{
				String pdfFileName = reportDate + " Raport.pdf";
				retrieveData(orderQuantity, foods, reportDate, quantities);
				orderViewModel.setFoods(foods);
				orderViewModel.setQuantities(quantities);
				model.addAttribute("orderViewModel", orderViewModel);
				try 
				{
					ExportToPDF.exportToPDF(pdfFileName, foods, quantities, reportDate);
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
