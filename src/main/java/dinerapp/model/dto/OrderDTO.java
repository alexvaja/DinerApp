package dinerapp.model.dto;

import java.util.Map;

import dinerapp.model.entity.Order;

public class OrderDTO {
	
	private Order order;
	private String toBeDeliveredFood;
	private Boolean taken;
	
	/*Bucatica lui Flaviusul*/
	private Integer orderId;
	private MenuDTO menuDTO;
	private Map<FoodDTO, Integer> quantities;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public MenuDTO getMenuDTO() {
		return menuDTO;
	}

	public void setMenuDTO(MenuDTO menuDTO) {
		this.menuDTO = menuDTO;
	}

	public Map<FoodDTO, Integer> getQuantities() {
		return quantities;
	}

	public void setQuantities(Map<FoodDTO, Integer> quantities) {
		this.quantities = quantities;
	}

	/*Aici se termina bucatica lui Flaviusul*/
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
		return "OrderDTO [toBeDeliveredFood=" + toBeDeliveredFood + ", taken=" + taken + ", orderId=" + orderId
				+ ", menuDTO=" + menuDTO + ", quantities=" + quantities + "]";
	}
}
