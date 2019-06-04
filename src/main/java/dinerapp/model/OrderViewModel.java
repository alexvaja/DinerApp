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
	
	private List<Food> foods;
	private List<Order> orders;
	private List<Integer> quantities;	
	private List<OrderQuantity> orderQuantity;

	private List<Order> ordersAfter4PM;
	private List<Order> ordersBefore4PM;
	
	private List<OrderQuantity> orderQuantityAfter4PM;
	private List<OrderQuantity> orderQuantityBefore4PM;
		
	private List<Integer> after4PMQuantities;
	private List<Integer> before4PMQuantities;
	
	private String date;

	public OrderViewModel() 
	{
		super();
		this.foods = new ArrayList<>();
		this.orders = new ArrayList<>();
		this.quantities = new ArrayList<>();
		this.orderQuantity = new ArrayList<>();
		this.ordersAfter4PM = new ArrayList<>();
		this.ordersBefore4PM = new ArrayList<>();
		this.after4PMQuantities = new ArrayList<>();
		this.before4PMQuantities = new ArrayList<>();
		this.orderQuantityAfter4PM = new ArrayList<>();
		this.orderQuantityBefore4PM = new ArrayList<>();
	}

	public List<Food> getFoods() {
		return foods;
	}

	public void setFoods(List<Food> foods) {
		this.foods = foods;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<Integer> getQuantities() {
		return quantities;
	}

	public void setQuantities(List<Integer> quantities) {
		this.quantities = quantities;
	}

	public List<OrderQuantity> getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(List<OrderQuantity> orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public List<Order> getOrdersAfter4PM() {
		return ordersAfter4PM;
	}

	public void setOrdersAfter4PM(List<Order> ordersAfter4PM) {
		this.ordersAfter4PM = ordersAfter4PM;
	}

	public List<Order> getOrdersBefore4PM() {
		return ordersBefore4PM;
	}

	public void setOrdersBefore4PM(List<Order> ordersBefore4PM) {
		this.ordersBefore4PM = ordersBefore4PM;
	}

	public List<OrderQuantity> getOrderQuantityAfter4PM() {
		return orderQuantityAfter4PM;
	}

	public void setOrderQuantityAfter4PM(List<OrderQuantity> orderQuantityAfter4PM) {
		this.orderQuantityAfter4PM = orderQuantityAfter4PM;
	}

	public List<OrderQuantity> getOrderQuantityBefore4PM() {
		return orderQuantityBefore4PM;
	}

	public void setOrderQuantityBefore4PM(List<OrderQuantity> orderQuantityBefore4PM) {
		this.orderQuantityBefore4PM = orderQuantityBefore4PM;
	}

	public List<Integer> getAfter4PMQuantities() {
		return after4PMQuantities;
	}

	public void setAfter4PMQuantities(List<Integer> after4pmQuantities) {
		after4PMQuantities = after4pmQuantities;
	}

	public List<Integer> getBefore4PMQuantities() {
		return before4PMQuantities;
	}

	public void setBefore4PMQuantities(List<Integer> before4pmQuantities) {
		before4PMQuantities = before4pmQuantities;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}	
}
