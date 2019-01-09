package dinerapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class NextDayReportController {

	@GetMapping("/nextDayReportView")
	public String openNextDayReportView(final Model model)
	{
		return "views/nextDayReportView";
	}
}
