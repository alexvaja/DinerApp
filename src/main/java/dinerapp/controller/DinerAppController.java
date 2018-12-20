package dinerapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class DinerAppController {

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  @PostMapping("/dinerapp")
  public String ceva(@RequestParam("submit") final String reqParam) {
    LOGGER.info("am intrat in post");
    LOGGER.info(reqParam);
    switch (reqParam) {
      case "food" :
        LOGGER.info("am intrat in cantina3");
        return "foodView";
    }
    return "dinerapp";
  }
  @GetMapping("/index")
  public String getDinerAppForm(final Model model) {
    LOGGER.info("am intrat in cantina");
    return "index";
  }
  @GetMapping("/")
  public String getDinerAppForm2(final Model model) {
    LOGGER.info("am intrat in cantina2");
    return "index";
  }
}
