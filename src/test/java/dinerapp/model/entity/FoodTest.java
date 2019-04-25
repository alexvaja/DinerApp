package dinerapp.model.entity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FoodTest {

	@Test
	public void testSizeIsIncreasing() {
		
		List<Food> foodToTest = new ArrayList<>();
		Food food = new Food();
		
		food.setId(1);
		food.setIngredients("ingredient1, ingredient2");
		food.setName("nume1");
		food.setPrice(10.0);
		food.setWeight(200);
		
		foodToTest.add(food);
		
		assertEquals(1, foodToTest.size());
	}
	
	@Test
	public void testFoodIsStoringData() {
		
		List<Food> foodToTest = new ArrayList<>();
		Food food = new Food();

		List<Dish> dishes = new ArrayList<>();
		Dish dish = new Dish();
		
		dish.setId(1);
		
		dishes.add(dish);
		
		List<OrderQuantity> foodss = new ArrayList<>();
		OrderQuantity foods = new OrderQuantity();
		
		List<Role> roles = new ArrayList<>();
		Role role = new Role();
		
		role.setId(1);
		role.setName("Nume role");
		
		roles.add(role);
		
		UserDiner userDiner = new UserDiner();
		userDiner.setId(1);
		userDiner.setName("nume1");
		userDiner.setPassword("parola");
		userDiner.setRoles(roles);
		
		Order order = new Order();
		order.setUserDiner(userDiner);
		
		foods.setId(1);
		foods.setQuantity(2);
		foods.setOrder(order);
		
		food.setId(1);
		food.setDishes(dishes);
		food.setFoodss(foodss);
		food.setIngredients("ingredient1, ingredient2");
		food.setName("nume1");
		food.setPrice(10.0);
		food.setWeight(200);
		
		foodToTest.add(food);
		
		assertEquals(food.getDishes(), foodToTest.get(0).getDishes());
		assertEquals(food.getFoodss(), foodToTest.get(0).getFoodss());
		assertEquals(food.getIngredients(), foodToTest.get(0).getIngredients());
		assertEquals(food.getName(), foodToTest.get(0).getName());
		
	}
	
	@Test
	public void testFoodNotNull() {
		
		List<Food> foodToTest = new ArrayList<>();
		Food food = new Food();
		
		foodToTest.add(food);
		
		assertNotNull(foodToTest);;
	}

}
