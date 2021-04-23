package dinerapp.aspect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public aspect CheckingBDOnStart {
	
	Boolean isValidConnection = false;
	Connection conn = null;
	Statement stmt;
	
	pointcut checkDBOnStart() : call(void dinerapp.Application.checkBD());
	
	before(): checkDBOnStart() {
		System.out.println("Before in CheckingBDOnStart Aspect");
		
		try {
			if (conn == null) {
				conn = DriverManager.getConnection(  
						"jdbc:mysql://localhost:3306/dinerapp","root","123456");
			}
			
			if (conn.isClosed()) {
				throw new NullPointerException();
			} else {
				isValidConnection = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	after(): checkDBOnStart() {
		System.out.println("After in CheckingBDOnStart Aspect");
		
		if (isValidConnection) {
			try {
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM user");
				
				if (rs.next()) {  
					while(rs.next()) {
						System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(3));
					}
				} else {
					ResultSet insertAdminUser = 
							stmt.executeQuery("INSERT INTO user VALUES (3, user, pass)");
					System.out.println("INSERAT");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static HttpSession session() {
	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    return attr.getRequest().getSession(true); // true == allow create
	}
}
