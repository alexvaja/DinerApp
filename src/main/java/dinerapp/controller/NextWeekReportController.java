package dinerapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

public class NextWeekReportController {

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  @GetMapping("/nextWeekReportView")
  public String getNextDayReports() {
    LOGGER.info("Am intrat in raportul pe saptamana viitoare");
    return "nextWeekReportView";
  }
}
