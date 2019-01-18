package dinerapp.model;

import java.util.ArrayList;
import java.util.List;

import dinerapp.model.entity.Food;
import dinerapp.model.entity.Order;

public class OrderViewModel {

	private List<Order> orders;
	private List<Food> foods;
	private List<Integer> quantities;
	
 	public OrderViewModel()
	{
		super();
		this.orders = new ArrayList<>();
	}
	public OrderViewModel(List<Order> orders)
	{
		super();
		this.orders = new ArrayList<>(orders);
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
	public void setQuantities(List<Integer> quantities) {
		this.quantities = quantities;
	}
	
	@Override
	public String toString() {
		return "OrderViewModel [orders=" + orders + ", foods=" + foods + ", quantities=" + quantities + "]";
	}
	
	
}
