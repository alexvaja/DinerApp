package dinerapp.model.entity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class UserDinerTest {

	@Test
	public void testSizeIsIncreasing() {

		List<UserDiner> userDinerToTest = new ArrayList<>();
		UserDiner userDiner = new UserDiner();
		
		userDiner.setId(1);
		userDiner.setName("name1");

		userDinerToTest.add(userDiner);

		assertEquals(1, userDinerToTest.size());
	}
	
	@Test
	public void testUserDinerIsStoringData() {
		
		List<UserDiner> userDinerToTest = new ArrayList<>();
		UserDiner userDiner = new UserDiner();
	
		userDiner.setId(1);
		userDiner.setName("name1");
		userDiner.setPassword("pass");
		
		userDinerToTest.add(userDiner);
		
		assertEquals(userDiner.getId(), userDinerToTest.get(0).getId());
		assertEquals("name1", userDinerToTest.get(0).getName());
		assertEquals("pass", userDinerToTest.get(0).getPassword());
	}
	
	@Test
	public void testUserDinerNotNull() {
		
		List<UserDiner> userDinerToTest = new ArrayList<>();
		UserDiner userDiner = new UserDiner();
		
		userDinerToTest.add(userDiner);
		
		assertNotNull(userDinerToTest);;
	}

}
