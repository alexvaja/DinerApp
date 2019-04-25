package dinerapp.model.entity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DishTest {

	@Test
	public void testSizeIsIncreasing() {
		
		List<Dish> dishToTest = new ArrayList<>();
		Dish dish = new Dish();
		
		dish.setId(1);
		
		dishToTest.add(dish);
		
		assertEquals(1, dishToTest.size());
	}
	
	@Test
	public void testDishIsStoringData() {
		
		List<Dish> dishToTest = new ArrayList<>();
		Dish dish = new Dish();
		
		Category category = new Category();
		
		category.setId(1);
		category.setName("category1");
		category.setPrice(100.0);
		
		List<Food> foods = new ArrayList<>();
		Food food = new Food();
		
		food.setId(1);
		food.setIngredients("ingredient1, ingredient2");
		food.setName("nume1");
		food.setPrice(15.0);
		food.setWeight(150);
		
		foods.add(food);
		
		Menu menu = new Menu();
		menu.setDate("1/1/1111");
		menu.setState("valid");
		menu.setTitle("Meniu");
		
		dish.setCategory(category);
		dish.setFoods(foods);
		dish.setId(1);
		dish.setMenu(menu);
		
		dishToTest.add(dish);
		
		assertEquals(dish.getFoods(), dishToTest.get(0).getFoods());
		assertEquals(dish.getMenu(), dishToTest.get(0).getMenu());
		
	}
	
	@Test
	public void testDishNotNull() {
		
		List<Dish> dishToTest = new ArrayList<>();
		Dish dish = new Dish();
		
		dishToTest.add(dish);
		
		assertNotNull(dishToTest);;
	}
	
	
	
}
