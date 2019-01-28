package dinerapp.model;

import java.util.ArrayList;
import java.util.List;

import dinerapp.model.entity.Order;
import dinerapp.model.entity.OrderQuantity;
import dinerapp.model.entity.UserDiner;

public class UserReportViewModel {

	private List<Order> orders;
	private List<UserDiner> users;
	private List<OrderQuantity> orderQuantity;
	private List<String> dates;
	
	public UserReportViewModel() {
		super();
		this.orders = new ArrayList<>();
		this.users = new ArrayList<>();
		this.orderQuantity = new ArrayList<>();
		this.dates = new ArrayList<>();
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

	public List<String> getDates() {
		return dates;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}

	@Override
	public String toString() {
		return "NextWeekViewModel [" + orders + ", " + users + ", " + orderQuantity + ", " + dates +"]";
	}
}
