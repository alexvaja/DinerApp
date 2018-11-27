package dinerapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class DinerAppController 
{
	@GetMapping("/index")
	public String getDinerAppForm(Model model) {
		System.out.println("am intrat in cantina");
		return "index";
	}
	
	@GetMapping("/")
	public String getDinerAppForm2(Model model) {
		System.out.println("am intrat in cantina2");
		return "index";
	}
	
	@PostMapping("/dinerapp")
	public String ceva(@RequestParam("submit") String reqParam)
	{
		System.out.println("am intrat in post");
		System.out.println(reqParam);
		switch(reqParam) {
		case "food":
			System.out.println("am intrat in cantina3");
			return "foodView";
		}
		return "dinerapp";
	}
	
//	@RequestMapping(value = "/foodView", method = RequestMethod.GET)
//	public String openFoodView()
//	{
//		return "foodView";
//	}
	
}
