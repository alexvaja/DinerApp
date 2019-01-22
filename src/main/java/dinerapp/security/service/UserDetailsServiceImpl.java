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

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserCantinaRepository userCantinaRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserDiner userDiner = getUserByName(userName);

		if (userDiner == null) {
			System.out.println("User not found! " + userName);
			throw new UsernameNotFoundException("User " + userName + " was not found in the database");
		}

		System.out.println("Found User: " + userDiner);

		List<Role> roleNames = this.getUserRoles(userDiner);

		System.out.println("User Roles: " + roleNames);

		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNames != null) {
			for (Role role : roleNames) {
				// ROLE_USER, ROLE_ADMIN,..
				GrantedAuthority authority = new SimpleGrantedAuthority(role.toString());
				grantList.add(authority);
			}
		}

		UserDetails userDetails = (UserDetails) new User(userDiner.getName(), EncrytedPasswordUtils.encrytePassword(userDiner.getPassword()), grantList);

		System.out.println("User Details: " + userDetails);

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
