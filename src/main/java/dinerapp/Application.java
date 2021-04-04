package dinerapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dinerapp.service.LoggingService;

@SpringBootApplication
public class Application implements CommandLineRunner 
{
	private final LoggingService greetingService;
	
	public Application(LoggingService greetingService) {
	    this.greetingService = greetingService;
	  }
	
	public static void main(final String[] args) 
	{
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception 
	{
		greetingService.appStart("Dinerapp");
	}

}
