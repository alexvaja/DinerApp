package dinerapp.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.annotation.SessionScope;

import dinerapp.model.entity.Food;
import dinerapp.model.entity.Order;
import dinerapp.model.entity.OrderQuantity;

@SessionScope
public class OrderViewModel 
{
	private List<Order> orders;
	private List<Food> foods;
	private List<Integer> quantities;
	private List<OrderQuantity> orderQuantity;
	private String date;
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public OrderViewModel()
	{
		super();
		this.orders = new ArrayList<>();
		this.foods = new ArrayList<>();
		this.quantities = new ArrayList<>();
		this.orderQuantity = new ArrayList<>();;
	}
	public OrderViewModel(List<Order> orders)
	{
		super();
		this.orders = new ArrayList<>(orders);
	}
	
 	public List<OrderQuantity> getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(List<OrderQuantity> orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	public List<Food> getFoods() {
		return foods;
	}
	public void setFoods(List<Food> foods) {
		this.foods = foods;
	}
	public List<Integer> getQuantities() {
		return quantities;
	}
	
	public Integer getByIndex(int index) {
		return quantities.get(index);
	}
	
	public void setQuantities(List<Integer> quantities) {
		this.quantities = quantities;
	}
	@Override
	public String toString() {
		return "OrderViewModel [orders=" + orders + ", foods=" + foods + ", quantities=" + quantities
				+ ", orderQuantity=" + orderQuantity + ", date=" + date + "]";
	}

}
