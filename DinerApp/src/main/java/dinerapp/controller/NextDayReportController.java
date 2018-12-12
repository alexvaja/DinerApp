package dinerapp.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NextDayReportController {

	@GetMapping("/nextDayReportView")
	public String getNextDayReports()
	{
		System.out.println("am intrat in raportul pe ziua urmatoare."
				+ "Mai ceva ca Hitler in Polonia cu panzerul");				
		return "nextDayReportView";
	}
}
