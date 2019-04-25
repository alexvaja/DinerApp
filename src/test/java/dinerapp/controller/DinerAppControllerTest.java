package dinerapp.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.security.Principal;

import org.junit.Test;
import org.springframework.ui.Model;


public class DinerAppControllerTest {

	@Test
	public void test() {
		

		DinerAppController dinerAppController = new DinerAppController();

		Model model = mock(Model.class);
		Principal principal = mock(Principal.class);
		//HttpSession session = mock(HttpSession.class);


		String filteredDinerApp = dinerAppController.loginPage(model, principal);

		assertEquals("views/loginView", filteredDinerApp);
	}

}
