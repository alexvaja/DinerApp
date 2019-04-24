package dinerapp.model.dto;

import java.util.Map;

import dinerapp.model.entity.Order;

public class OrderDTO {
	
	private Order order;
	private String toBeDeliveredFood;
	private Boolean taken;
	
	/*Bucatica lui Flaviusul*/
	private Integer orderID;
	private MenuDTO menuDTO;
	private Map<FoodDTO, Integer> quanTITIES;
		
	public Integer getOrderID() {
		return orderID;
	}

	public void setOrderID(Integer orderID) {
		this.orderID = orderID;
	}

	public MenuDTO getMenuDTO() {
		return menuDTO;
	}

	public void setMenuDTO(MenuDTO menuDTO) {
		this.menuDTO = menuDTO;
	}

	public Map<FoodDTO, Integer> getQuanTITIES() {
		return quanTITIES;
	}

	public void setQuantities(Map<FoodDTO, Integer> quanTITIES) {
		this.quanTITIES = quanTITIES;
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
		return "OrderDTO [" + order + ", " + toBeDeliveredFood + ", " + taken + "]";
	}
}
