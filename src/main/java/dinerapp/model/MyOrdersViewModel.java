package dinerapp.model;

import dinerapp.model.dto.OrderDTO;

public class MyOrdersViewModel {

	private OrderDTO orderDTO;

	public OrderDTO getOrderDTO() {
		return orderDTO;
	}

	public void setOrderDTO(OrderDTO orderDTO) {
		this.orderDTO = orderDTO;
	}

	@Override
	public String toString() {
		return "MyOrdersViewModel [orderDTO=" + orderDTO + "]";
	}	
}
