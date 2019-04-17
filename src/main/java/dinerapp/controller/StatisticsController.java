package dinerapp.controller;

import java.util.ArrayList;
import java.util.Collections;
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
import dinerapp.model.dto.StatisticDTO;
import dinerapp.model.entity.Order;
import dinerapp.model.entity.UserDiner;
import dinerapp.repository.OrderRepository;
import dinerapp.repository.UserCantinaRepository;
import dinerapp.security.utils.UserComparer;

@Controller
public class StatisticsController {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private OrderRepository orderRepo;
	@Autowired
	private UserCantinaRepository userDinerRepo;

	@ExceptionHandler({ NewSessionException.class })
	public String sessionError() {
		String mesaj = "incercare de acces nepermis";
		LOGGER.error(mesaj);
		return "views/loginView";
	}

	private List<Order> getListOfOrders() {
		final Iterable<Order> list = orderRepo.findAll();
		final List<Order> orderList = new ArrayList<>();
		for (final Order order : list)
			orderList.add(order);
		return orderList;
	}
	private List<UserDiner> getListOfUsers() {
		final Iterable<UserDiner> list = userDinerRepo.findAll();
		final List<UserDiner> userList = new ArrayList<>();
		for (final UserDiner user : list)
			userList.add(user);
		return userList;
	}
	
	private void sortUserList(List<UserDiner> users)
	{
		Collections.sort(users, new UserComparer());
	}

	@SessionScope
	@GetMapping("/statisticsView")
	public String getStatistics(Model model, HttpSession session) throws NewSessionException 
	{
		LOGGER.info("Am intrat in getStatistics");
		if (session.isNew())
			throw new NewSessionException();

		StatisticsViewModel statisticsViewModel = new StatisticsViewModel();
		List<Order> orders = getListOfOrders();
		List<UserDiner> users = getListOfUsers();

		List<Integer> placedOrders = new ArrayList<>(users.size());
		List<Integer> pickedUpOrders = new ArrayList<>(users.size());
		
		sortUserList(users);
		for (int index = 0; index < users.size(); index++) {
			LOGGER.info("PlacedOrders1 :" + placedOrders);
			LOGGER.info("PickedUpOrder1" + pickedUpOrders);
		}
		

		for (int i = 0; i < users.size(); i++) {
			LOGGER.info("user " + i + ":" + users.get(i));
			LOGGER.info("---------------------------------------");
			
			int numberOfPlacedOrders = 0;
			int numberOfPickedUpOrders = 0;
			
			for (int index = 0; index < orders.size(); index++) 
			{
				LOGGER.info("order " + index + ":" + orders.get(index));
				if (orders.get(index).getUserDiner().equals(users.get(i))) 
				{
					numberOfPlacedOrders++;
					if (orders.get(index).getTaken() == true) {
						numberOfPickedUpOrders++;
					}
				}
			}
			
			placedOrders.add(new Integer(numberOfPlacedOrders));
			pickedUpOrders.add(new Integer(numberOfPickedUpOrders));
			
			}
		
		List<String> x = new ArrayList<>();
		
		for (int index = 0; index < users.size(); index++) {
			x.add(placedOrders.get(index) + "/" + pickedUpOrders.get(index));
		}

		System.out.println("PlacedOrders2 :" + placedOrders);
		System.out.println("PickedUpOrder2" + pickedUpOrders);

		List<StatisticDTO> statistics = new ArrayList<>();
		
		for (int index = 0; index < users.size(); index++) {
			//StatisticDTO stat = new StatisticDTO(users.get(index), placedOrders.get(index), pickedUpOrders.get(index));
			if(users.get(index).getRoles().get(0).getName().equals("employee"))
				statistics.add(new StatisticDTO(users.get(index), placedOrders.get(index), pickedUpOrders.get(index)));
		}
		
		statisticsViewModel.setStatistics(statistics);
		
//		statisticsViewModel.setPickedUpOrders(pickedUpOrders);
//		statisticsViewModel.setPlacedOrders(placedOrders);
//		statisticsViewModel.setUsers(users);
//		statisticsViewModel.setX(x);
		
//		System.out.println("Aici afisez");
//		
//		System.out.println("usersss :" + users);
//			
//
//		System.out.println();
//		System.out.println(statisticsViewModel);
		session.setAttribute("statisticsViewModel", statisticsViewModel);

		return "views/statisticsView";
	}

}
