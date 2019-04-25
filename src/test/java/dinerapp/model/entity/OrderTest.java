package dinerapp.model.entity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OrderTest {


	@Test
	public void testSizeIsIncreasing() {

		List<Order> orderToTest = new ArrayList<>();
		Order order = new Order();
		
		order.setId(1);

		orderToTest.add(order);

		assertEquals(1, orderToTest.size());
	}
	
	@Test
	public void testOrderIsStoringData() {
		
		List<Order> orderToTest = new ArrayList<>();
		Order order = new Order();

		List<OrderQuantity> orderQuantities = new ArrayList<>();
		OrderQuantity orderQuantity = new OrderQuantity();		

		orderQuantity.setId(1);
		
		orderQuantities.add(orderQuantity);
		
		
		UserDiner userDiner = new UserDiner();
		
		userDiner.setId(1);
		userDiner.setName("name1");
		userDiner.setPassword("pass");
		
		order.setId(1);
		order.setDate("11/11/1111");
		order.setOrderQuantities(orderQuantities);
		order.setTaken(true);
		order.setUserDiner(userDiner);
		
		orderToTest.add(order);
		
		assertEquals(order.getId(), orderToTest.get(0).getId());
		assertEquals("11/11/1111", orderToTest.get(0).getDate());
		assertEquals(true, orderToTest.get(0).getTaken());
		assertEquals(order.getOrderQuantities(), orderToTest.get(0).getOrderQuantities());
		assertEquals(order.getUserDiner(), orderToTest.get(0).getUserDiner());

	}
	
	@Test
	public void testOrderNotNull() {
		
		List<Order> orderToTest = new ArrayList<>();
		Order order = new Order();
		
		orderToTest.add(order);
		
		assertNotNull(orderToTest);;
	}
	

}
