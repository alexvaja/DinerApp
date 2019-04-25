package dinerapp.model.entity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class RoleTest {

	@Test
	public void testSizeIsIncreasing() {

		List<Role> roleToTest = new ArrayList<>();
		Role role = new Role();
		
		role.setId(1);
		role.setName("name1");

		roleToTest.add(role);

		assertEquals(1, roleToTest.size());
	}
	
	@Test
	public void testRoleIsStoringData() {
		
		List<Role> roleToTest = new ArrayList<>();
		Role role = new Role();
	
		role.setId(1);
		role.setName("name1");
		
		roleToTest.add(role);
		
		assertEquals(role.getId(), roleToTest.get(0).getId());
		assertEquals("name1", roleToTest.get(0).getName());
	}
	
	@Test
	public void testRoleNotNull() {
		
		List<Role> roleToTest = new ArrayList<>();
		Role role = new Role();
		
		roleToTest.add(role);
		
		assertNotNull(roleToTest);;
	}

}
