//package dinerapp.security.service;
//
//import java.util.ArrayList;
//import java.util.List;
//import dinerapp.entity.RoleCantina;
//import dinerapp.entity.UserCantina;
//import dinerapp.repository.UserCantinaRepository;
//import dinerapp.security.utils.EncrytedPasswordUtils;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//	@Autowired
//	private UserCantinaRepository userCantinaRepository;
//
//	@Override
//	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//		UserCantina userCantina = getUserByName(userName);
//
//		if (userCantina == null) {
//			System.out.println("User not found! " + userName);
//			throw new UsernameNotFoundException("User " + userName + " was not found in the database");
//		}
//
//		System.out.println("Found User: " + userCantina);
//
//		List<RoleCantina> roleNames = this.getUserRoles(userCantina);
//
//		System.out.println("User Roles: " + roleNames);
//
//		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
//		if (roleNames != null) {
//			for (RoleCantina role : roleNames) {
//				// ROLE_USER, ROLE_ADMIN,..
//				GrantedAuthority authority = new SimpleGrantedAuthority(role.toString());
//				grantList.add(authority);
//			}
//		}
//
//		UserDetails userDetails = (UserDetails) new User(userCantina.getName(), EncrytedPasswordUtils.encrytePassword(userCantina.getPassword()), grantList);
//
//		System.out.println("User Details: " + userDetails);
//
//		return userDetails;
//	}
//
//	private UserCantina getUserByName(String userName) {
//		Iterable<UserCantina> userList = userCantinaRepository.findAll();
//
//		for (UserCantina user : userList) {
//			System.out.println(user.getName() + " " + userName);
//			if (user.getName().equals(userName)) {
//				return user;
//			}
//		}
//
//		return null;
//	}
//
//	private List<RoleCantina> getUserRoles(UserCantina userCantina) {
//		if (userCantina != null) {
//			return userCantina.getRoles();
//		}
//
//		return null;
//	}
//}
