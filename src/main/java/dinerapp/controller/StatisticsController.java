package dinerapp.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticsController 
{
	@GetMapping("/statisticsView")
	public String getstatistics(Model model, HttpSession session) 
	{
		return "views/statisticsView";
	}
}
