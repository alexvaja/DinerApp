package dinerapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NextWeekReportController 
{
	@GetMapping("/nextWeekReportView")
	public String openNextWeekReportView(Model model)
	{
		return "views/nextWeekReportView";
	}
	
	@PostMapping("/nextWeekReportView")
	public String openNextWeekReportyyView(Model model)
	{
		return "views/nextWeekReportView";
	}
}
 