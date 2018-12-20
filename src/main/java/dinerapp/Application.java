package dinerapp;

//import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  //static Logger LOGGER = Logger.getLogger(Application.class);

  public static void main(final String[] args) {
    //LOGGER.info("STARTING DINER APP");
    SpringApplication.run(Application.class, args);
  }

}
