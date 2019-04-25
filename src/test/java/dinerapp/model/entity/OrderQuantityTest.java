package dinerapp.model.entity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OrderQuantityTest {

	@Test
	public void testSizeIsIncreasing() {

		List<OrderQuantity> orderQuantityToTest = new ArrayList<>();
		OrderQuantity orderQuantity = new OrderQuantity();

		Integer quantity = 2;
		
		orderQuantity.setId(1);
		orderQuantity.setQuantity(quantity);

		orderQuantityToTest.add(orderQuantity);

		assertEquals(1, orderQuantityToTest.size());
	}
	
	@Test
	public void testOrderQuantityIsStoringData() {
		
		List<OrderQuantity> orderQuantityToTest = new ArrayList<>();
		OrderQuantity orderQuantity = new OrderQuantity();		

		Food foodd = new Food();
		foodd.setId(1);
		foodd.setIngredients("ingredient1, ingredient2");
		foodd.setName("Name1");
		foodd.setPrice(10.0);
		foodd.setWeight(150);
		
		Order order = new Order();
		order.setDate("11/11/1111");
		order.setTaken(true);
		
		Integer quantity = 1;
		
		orderQuantity.setId(1);
		orderQuantity.setFoodd(foodd);
		orderQuantity.setOrder(order);
		orderQuantity.setQuantity(quantity);

		
		orderQuantityToTest.add(orderQuantity);
		
		assertEquals(orderQuantity.getId(), orderQuantityToTest.get(0).getId());
		assertEquals(orderQuantity.getFoodd(), orderQuantityToTest.get(0).getFoodd());
		assertEquals(orderQuantity.getOrder(), orderQuantityToTest.get(0).getOrder());
		assertEquals(orderQuantity.getQuantity(), orderQuantityToTest.get(0).getQuantity());
	}
	
	@Test
	public void testOrderQuantityNotNull() {
		
		List<OrderQuantity> orderQuantityToTest = new ArrayList<>();
		OrderQuantity orderQuantity = new OrderQuantity();
		
		orderQuantityToTest.add(orderQuantity);
		
		assertNotNull(orderQuantityToTest);;
	}
	
}
