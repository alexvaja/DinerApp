package dinerapp.model.entity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MenuTest {

	@Test
	public void testSizeIsIncreasing() {

		List<Menu> menuToTest = new ArrayList<>();
		Menu menu = new Menu();

		menu.setId(1);
		menu.setDate("1/1/1111");
		menu.setState("Saved");
		menu.setTitle("titlu1");

		menuToTest.add(menu);

		assertEquals(1, menuToTest.size());
	}
	
	@Test
	public void testMenuIsStoringData() {
		
		List<Menu> menuToTest = new ArrayList<>();
		Menu menu = new Menu();

		List<Dish> dishes = new ArrayList<>();
		Dish dish = new Dish();
		
		dish.setId(1);
		
		dishes.add(dish);
		
		
		menu.setId(1);
		menu.setDate("11/11/1111");
		menu.setState("published");
		menu.setTitle("menu1");
		menu.setDishes(dishes);
		
		menuToTest.add(menu);
		
		assertEquals(menu.getDishes(), menuToTest.get(0).getDishes());
		assertEquals(menu.getDate(), menuToTest.get(0).getDate());
		assertEquals(menu.getId(), menuToTest.get(0).getId());
		assertEquals("published", menuToTest.get(0).getState());
		assertEquals("menu1", menuToTest.get(0).getTitle());

	}
	
	@Test
	public void testMenuNotNull() {
		
		List<Menu> menuToTest = new ArrayList<>();
		Menu food = new Menu();
		
		menuToTest.add(food);
		
		assertNotNull(menuToTest);;
	}

}
