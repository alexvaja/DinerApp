package junit;

import static org.junit.Assert.*;

import org.junit.Test;

import dinerapp.model.entity.Category;

public class CategoryTest {

	Category category = new Category();
	
	@Test
	public void testGetId() {
		Integer result = category.getId();
		assertEquals(Double.valueOf(1), Double.valueOf(result));
	}

//	@Test
//	public void testSetId() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetName() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetName() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetPrice() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetPrice() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetDishes() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetDishes() {
//		fail("Not yet implemented");
//	}

}
