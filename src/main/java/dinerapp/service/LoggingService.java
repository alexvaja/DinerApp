package dinerapp.service;

import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

import dinerapp.annotation.LogEntryExit;

@Service
public class LoggingService 
{
	@LogEntryExit(showArgs = true, showResult = true, unit = ChronoUnit.MILLIS, showExecutionTime = true)
	public String appStart(String name) 
	{
		return "Hello, " +name + "!";
	}

}
