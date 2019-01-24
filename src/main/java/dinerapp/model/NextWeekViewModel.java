package dinerapp.model;

import java.util.ArrayList;
import java.util.List;

import dinerapp.model.entity.Order;
import dinerapp.model.entity.OrderQuantity;
import dinerapp.model.entity.UserDiner;

public class NextWeekViewModel {

	private List<Order> orders;
	private List<UserDiner> users;
	private List<OrderQuantity> orderQuantity;
	
	public NextWeekViewModel() {
		super();
		this.orders = new ArrayList<>();
		this.users = new ArrayList<>();
		this.orderQuantity = new ArrayList<>();
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

	public List<OrderQuantity> getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(List<OrderQuantity> orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	@Override
	public String toString() {
		return "NextWeekViewModel [" + orders + ", " + users + ", " + orderQuantity + "]";
	}
}
