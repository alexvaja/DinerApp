package dinerapp.controller;

import java.security.Principal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dinerapp.model.dto.UserDTO;
import dinerapp.security.config.DBConnection;

@Controller
public class RegisterController {

	@GetMapping("/registerView")
	public String getRegisterView(Model model) throws SQLException {
		
		model.addAttribute("userDTO", new UserDTO());
		return "views/RegisterView";
	}

	@PostMapping("/registerView")
	public String postRegisterView(Model model, Principal principal, @ModelAttribute UserDTO userDTO,
			@RequestParam("submit") String reqParam) throws SQLException {
		
		model.addAttribute("userDTO", userDTO);
		model.addAttribute("okPassword", true);
		
		
		switch (reqParam) {
		case "Register":

			String querry = "INSERT INTO user (user_name, user_password, user_repeat_password) VALUES ( '" + userDTO.getUsername()
					+ "', '" + userDTO.getPassword() + "', '" + userDTO.getRepeatPassword() + "')";
			String q = "Select * From user";
			String query = "Select * From user where user_name = '" + userDTO.getUsername() + "'";

			//System.out.println("MUIE PSD" + userDTO.getUsername() + " " + userDTO.getPassword() + " " + userDTO.getRepeatPassword());
			
			ResultSet result = executeQuery(q);

			if (userDTO.getPassword() != userDTO.getRepeatPassword()) {
				model.addAttribute("q", result.getMetaData());
				model.addAttribute("okPassword", false);
			}
			
			result = executeQuery(query);
			
			if (!searchForUsername(userDTO.getUsername())) {
				model.addAttribute("q", result.getMetaData());
				model.addAttribute("okPassword", false);
			}
			
			result = executeQuery(querry);
			model.addAttribute("q", result.getMetaData());
			model.addAttribute("okPassword", false);
		}
		return "views/RegisterView";
	}
	
	private Boolean searchForUsername(String username) throws SQLException {
		
		String query = "Select * From user where user_name = '" + username + "'";
		
		ResultSet result = executeQuery(query);
		result.first();
		if (result.getString("user_name") == username) {
			return false;
		}

		return true;
	}
	
	private ResultSet executeQuery(String query) throws SQLException {
		
		Connection connection = DBConnection.getConnctionToDB();
		Statement stmt = connection.createStatement();
		ResultSet result = stmt.executeQuery(query);
		//connection.close();
		
		return result;
	}
}
