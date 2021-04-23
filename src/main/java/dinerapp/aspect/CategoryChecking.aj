package dinerapp.aspect;

public aspect CategoryChecking {

	pointcut checkInputCategory(): 
		(call(* dinerapp.controller.CategoryController.*()));
	
	before(): checkInputCategory() {
		System.out.println("CHECKING METHOD");
	}
}
