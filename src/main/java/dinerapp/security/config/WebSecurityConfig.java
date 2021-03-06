package dinerapp.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	//UserDetailsServiceImpl userDetailsService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		// Setting Service to find User in the database.
		// And Setting PassswordEncoder
		//auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();
		
		http.authorizeRequests().antMatchers("/", "/login", "/logout").permitAll();
		http.authorizeRequests().antMatchers("/categoryView").authenticated();
		http.authorizeRequests().antMatchers("/foodView").permitAll();
		http.authorizeRequests().antMatchers("/insertView").permitAll();
		http.authorizeRequests().antMatchers("/loginView").authenticated();
		http.authorizeRequests().antMatchers("/menuView").authenticated();
		http.authorizeRequests().antMatchers("/nextDayReportView").authenticated();
		http.authorizeRequests().antMatchers("/sessionTimeoutView").authenticated();
		http.authorizeRequests().antMatchers("/userReportView").authenticated();
		http.authorizeRequests().antMatchers("/viewMenuView").authenticated();
		http.authorizeRequests().antMatchers("/userReportView").authenticated();
		http.authorizeRequests().antMatchers("/reportView").authenticated();
		http.authorizeRequests().antMatchers("/index").authenticated();
		http.authorizeRequests().antMatchers("/statisticsView").authenticated();
		
		http.authorizeRequests().antMatchers("/employeeOrderView").access("hasRole('ROLE_USER')");
		http.authorizeRequests().antMatchers("/myOrdersView").access("hasRole('ROLE_USER')");
		
		
		// /userInfo page requires login as ROLE_USER or ROLE_ADMIN.
		// If no login, it will redirect to /login page.
		http.authorizeRequests().antMatchers("/userInfo").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");

		// For ADMIN only.
		http.authorizeRequests().antMatchers("/admin").access("hasRole('ROLE_ADMIN')");

		// When the user has logged in as XX.
		// But access a page that requires role YY,
		// AccessDeniedException will be thrown.
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/400");//

		// Config for Login Form
		http.authorizeRequests().and().formLogin()//
				// Submit URL of login page.
				.loginProcessingUrl("/j_spring_security_check") // Submit URL
				.loginPage("/login")//
				.defaultSuccessUrl("/index")//
				.failureUrl("/login?error=true")//
				.usernameParameter("username")//
				.passwordParameter("password")
				// Config for Logout Page
				.and().logout().logoutUrl("/logout").logoutSuccessUrl("/logoutSuccessful")
				.deleteCookies("JSESSIONID", "JWT").clearAuthentication(true).invalidateHttpSession(true);
				/*.deleteCookies("auth_code", "JSESSIONID").clearAuthentication(true).invalidateHttpSession(true);*/
	}
}
