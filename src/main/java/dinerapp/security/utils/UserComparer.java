package dinerapp.security.utils;

import java.util.Comparator;

import dinerapp.model.entity.UserDiner;

public class UserComparer implements Comparator <UserDiner>{

	@Override
	public int compare(UserDiner firstUser, UserDiner secondUser) {
		
		return firstUser.getName().compareTo(secondUser.getName());
	}

}
