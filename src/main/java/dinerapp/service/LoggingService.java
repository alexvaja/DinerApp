package dinerapp.service;

import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

import dinerapp.annotation.LogEntryExit;

@Service
public class GreetingService 
{
	@LogEntryExit(showArgs = true, showResult = true, unit = ChronoUnit.MILLIS)
	public String greet(String name) 
	{
		return "Hello, " + resolveName(name) + "!";
	}

	public String resolveName(String name) 
	{
		return !name.isEmpty() ? name : "world";
	}

}
