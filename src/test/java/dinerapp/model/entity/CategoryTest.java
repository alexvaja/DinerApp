package dinerapp.model.entity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CategoryTest {

	@Test
	public void testSizeIsIncreasing() {

		List<Category> categoryToTest = new ArrayList<>();
		Category category = new Category();

		category.setId(1);
		category.setName("testing");
		category.setPrice(100.0);

		categoryToTest.add(category);

		assertEquals(1, categoryToTest.size());
	}

	@Test
	public void testCategoryIsStoringData() {

		List<Category> categoryToTest1 = new ArrayList<>();
		Category category1 = new Category();

		List<Food> foodToTest1 = new ArrayList<>();

		Food food = new Food();
		food.setId(1);
		food.setIngredients("cartofi, mamaliga");
		food.setName("cartofi cu mamaliga");
		food.setPrice(200.0);
		food.setWeight(100);

		foodToTest1.add(food);

		Menu menu = new Menu();
		menu.setDate("12/05/1998");
		menu.setId(1);
		menu.setState("True");
		menu.setTitle("menu test");

		List<Dish> dishToTest1 = new ArrayList<>();

		Dish dish = new Dish();
		dish.setCategory(category1);
		dish.setFoods(foodToTest1);
		dish.setId(1);
		dish.setMenu(menu);

		dishToTest1.add(dish);

		category1.setId(2);
		category1.setName("testing1");
		category1.setPrice(150.0);
		category1.setDishes(dishToTest1);

		categoryToTest1.add(category1);

		assertEquals("testing1", categoryToTest1.get(0).getName().toString());
		assertEquals("150.0", categoryToTest1.get(0).getPrice().toString());
		// assertEquals(dish, categoryToTest1.get(0).getDishes());
	}

	@Test
	public void testCategoryNotNull() {
		
		List<Category> categoryToTest2 = new ArrayList<>();
		Category category2= new Category();
		
		categoryToTest2.add(category2);
		
		assertNotNull(categoryToTest2);;
	}

}
