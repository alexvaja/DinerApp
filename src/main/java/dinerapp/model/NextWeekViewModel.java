package dinerapp.model;

import java.util.ArrayList;
import java.util.List;

import dinerapp.model.entity.Order;
import dinerapp.model.entity.UserDiner;

public class NextWeekViewModel {

	private List<Order> orders;
	private List<UserDiner> users;
	
	public NextWeekViewModel() {
		super();
		orders = new ArrayList<>();
		users = new ArrayList<>();
		
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<UserDiner> getUsers() {
		return users;
	}

	public void setUsers(List<UserDiner> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "NextWeekViewModel [" + orders + ", " + users + "]";
	}
}
