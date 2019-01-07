package dinerapp.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NextDayReportController {

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  @GetMapping("/nextDayReportView")
  public String getNextDayReports() {
    LOGGER.info("am intrat in raportul pe ziua urmatoare." + "Mai ceva ca Hitler in Polonia cu panzerul");
    return "views/nextDayReportView";
  }
}
