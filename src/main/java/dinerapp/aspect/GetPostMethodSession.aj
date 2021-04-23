package dinerapp.aspect;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public aspect GetPostMethodSession {
	
	UserDetails user = null;
	
	pointcut checkController() : call(String dinerapp.controller.*.*(..)) || 
								 call(String dinerapp.controller.*.*(..));
	
	before(): checkController() {
		System.out.println("Check Controller");
		UserDetails user = currentUserDetails();
		
		if (user.isAccountNonExpired()) {
			
		}
	}
	
	after(): checkController() {
		
	}
	
	public static UserDetails currentUserDetails(){
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    Authentication authentication = securityContext.getAuthentication();
	    if (authentication != null) {
	        Object principal = authentication.getPrincipal();
	        return principal instanceof UserDetails ? (UserDetails) principal : null;
	    }
	    return null;
	}
}
