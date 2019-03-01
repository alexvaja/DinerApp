package dinerapp.security.service;

import java.util.ArrayList;
import java.util.List;

import dinerapp.model.entity.Role;
import dinerapp.model.entity.UserDiner;
import dinerapp.repository.UserCantinaRepository;
import dinerapp.security.utils.EncrytedPasswordUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserCantinaRepository userCantinaRepository;
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserDiner userDiner = getUserByName(userName);

		if (userDiner == null) {
			LOGGER.error("User not found! " + userName);
			throw new UsernameNotFoundException("User " + userName + " was not found in the database");
		}
		LOGGER.info("Found User: " + userDiner);
		List<Role> roleNames = this.getUserRoles(userDiner);
		LOGGER.info("User Roles: " + roleNames);

		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		List<Role> roles = new ArrayList<>();
		
		if (roleNames != null) {
			for (Role role : roleNames) {
				// ROLE_USER, ROLE_ADMIN,..
				roles.add(role);
				GrantedAuthority authority = new SimpleGrantedAuthority(role.getName().toString());
				grantList.add(authority);
			}
		}
		
		for (GrantedAuthority authority : grantList) {
			if (authority.getAuthority().toString().equals("employee")) {
				throw new UsernameNotFoundException("User " + userName + " was not found in the database");
			}
		}
		
		UserDetails userDetails = (UserDetails) new User(userDiner.getName(), EncrytedPasswordUtils.encrytePassword(userDiner.getPassword()), grantList);
		LOGGER.info("User Details: " + userDetails);
		return userDetails;
	}

	private UserDiner getUserByName(String userName) {
		Iterable<UserDiner> userList = userCantinaRepository.findAll();

		for (UserDiner userDiner : userList) {
			System.out.println(userDiner.getName() + " " + userName);
			if (userDiner.getName().equals(userName)) {
				return userDiner;
			}
		}
		return null;
	}

	private List<Role> getUserRoles(UserDiner userDiner) {
		if (userDiner != null) {
			return userDiner.getRoles();
		}
		return null;
	}
}
