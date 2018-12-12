package dinerapp.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class NextWeekReportController {
	
	@GetMapping("/nextWeekReportView")
	public String getNextDayReports()
	{
		System.out.println("Am intrat in raportul pe saptamana viitoare");
		return "nextWeekReportView";
	}
}
