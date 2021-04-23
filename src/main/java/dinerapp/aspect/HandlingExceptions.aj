package dinerapp.aspect;

public aspect HandlingExceptions {
	
	Boolean b = false;
	
	pointcut function() : call(void dinerapp.Application.checkBD(*));
	
	before(): function() {
		
	}
	
	after(): function() {
		System.out.println("Works after code");
		System.out.println(b);
	}

}
