package dinerapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NextWeekReportController 
{
	@GetMapping("/nextWeekReportView")
	public String openNextWeekReportView(final Model model)
	{
		return "views/nextWeekReportView";
	}
}
 