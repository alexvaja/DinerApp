package dinerapp.model;

import java.util.ArrayList;
import java.util.List;

import dinerapp.model.entity.Order;
import dinerapp.model.entity.OrderQuantity;

public class UserReportViewModel {

	private List<Order> orders;
	private List<OrderQuantity> orderQuantity;
	private List<String> dates;
	
	public UserReportViewModel() {
		super();
		this.orders = new ArrayList<>();
		this.orderQuantity = new ArrayList<>();
		this.dates = new ArrayList<>();
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
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
		return "NextWeekViewModel [" + orders + ", " + orderQuantity + ", " + dates +"]";
	}
}
