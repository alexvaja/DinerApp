package dinerapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dinerapp.repository.UserCantinaRepository;
import dinerapp.service.LoggingService;

@SpringBootApplication
public class Application implements CommandLineRunner 
{
	private final LoggingService greetingService;
	
	@Autowired
	private static UserCantinaRepository userRepository;
	
	public Application(LoggingService greetingService) {
	    this.greetingService = greetingService;
	}
	
	//public Application() {
		//this.greetingService = null;
	//}

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
		Application.checkBD();
	}

	@Override
	public void run(String... args) throws Exception {
		greetingService.appStart("Dinerapp");
	}
	
	public static void checkBD() {
		System.out.println("CHECK BD METHOD");
		
	}

}
