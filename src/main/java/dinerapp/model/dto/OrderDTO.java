package dinerapp.model.dto;

import dinerapp.model.entity.Order;

public class OrderDTO {
	
	private Order order;
	private String toBeDeliveredFood;
	private Boolean taken;
	
	public OrderDTO() {
		super();
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getToBeDeliveredFood() {
		return toBeDeliveredFood;
	}

	public void setToBeDeliveredFood(String toBeDeliveredFood) {
		this.toBeDeliveredFood = toBeDeliveredFood;
	}

	public Boolean getTaken() {
		return taken;
	}

	public void setTaken(Boolean taken) {
		this.taken = taken;
	}

	@Override
	public String toString() {
		return "OrderDTO [" + order + ", " + toBeDeliveredFood + ", " + taken + "]";
	}
}
