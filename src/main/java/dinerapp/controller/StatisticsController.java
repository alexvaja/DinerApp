package dinerapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.annotation.SessionScope;

import dinerapp.exceptions.NewSessionException;
import dinerapp.model.StatisticsViewModel;
import dinerapp.model.entity.Order;
import dinerapp.model.entity.UserDiner;
import dinerapp.repository.OrderRepository;
import dinerapp.repository.UserCantinaRepository;

@Controller
public class StatisticsController 
{
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private OrderRepository orderRepo;
	@Autowired
	private UserCantinaRepository userDinerRepo;
	
	@ExceptionHandler({ NewSessionException.class })
	public String sessionError() 
	{	
		String mesaj = "incercare de acces nepermis";
		LOGGER.error(mesaj);
		return "views/loginView";
	}
	private List<Order> getListOfOrders() 
	{
		final Iterable<Order> list = orderRepo.findAll();
		final List<Order> orderList = new ArrayList<>();
		for (final Order order : list)
			orderList.add(order);
		return orderList;
	}
	private List<UserDiner> getListOfUsers()
	{
		final Iterable<UserDiner> list = userDinerRepo.findAll();
		final List<UserDiner> userList = new ArrayList<>();
		for(final UserDiner user:list)
			userList.add(user);
		return userList;
	}
	
	@SessionScope
	@GetMapping("/statisticsView")
	public String getStatistics(Model model, HttpSession session) throws NewSessionException 
	{
		LOGGER.info("Am intrat in getStatistics");
		if (session.isNew()) 
			throw new NewSessionException();			

		StatisticsViewModel statisticsViewModel = new StatisticsViewModel();		
		List<Order>orders = getListOfOrders();
		List<UserDiner>users = getListOfUsers();	
		List<Integer> placedOrders = new ArrayList<Integer>();
		List<Integer> pickedUpOrders = new ArrayList<Integer>();
		
		for(int index = 0; index < users.size(); index++)
		{
			placedOrders.add(0);
			pickedUpOrders.add(0);
		}
		
		for(UserDiner user : users)
		{
			for(int i = 0; i < orders.size(); i++)
			{
				if(orders.get(i).getUserDiner().equals(user))
				{
					int value = placedOrders.get(i);
					value++;
					placedOrders.add(i, value);
					if(orders.get(i).getTaken() == true)
					{
						int var = pickedUpOrders.get(i);
						var++;
						pickedUpOrders.add(i,var);
					}
				}
			}
		}
		
		statisticsViewModel.setPickedUpOrders(pickedUpOrders);
		statisticsViewModel.setPlacedOrders(placedOrders);
		statisticsViewModel.setUsers(users);
		session.setAttribute("statisticsViewModel", statisticsViewModel);
		
		return "views/statisticsView";
	}

}
