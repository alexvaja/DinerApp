package dinerapp.model.dto;

import dinerapp.model.entity.UserDiner;

public class StatisticDTO {

	private UserDiner user;
	private Integer placedOrder;
	private Integer pickedUpOrder;

	public StatisticDTO() {
		super();
	}

	public StatisticDTO(UserDiner user, Integer placedOrder, Integer pickedUpOrder) {
		super();
		this.user = user;
		this.placedOrder = placedOrder;
		this.pickedUpOrder = pickedUpOrder;
	}

	public UserDiner getUser() {
		return user;
	}

	public void setUser(UserDiner user) {
		this.user = user;
	}

	public Integer getPlacedOrder() {
		return placedOrder;
	}

	public void setPlacedOrder(Integer placedOrder) {
		this.placedOrder = placedOrder;
	}

	public Integer getPickedUpOrder() {
		return pickedUpOrder;
	}

	public void setPickedUpOrder(Integer pickedUpOrder) {
		this.pickedUpOrder = pickedUpOrder;
	}

	@Override
	public String toString() {
		return "StatisticDTO [" + user + ", " + placedOrder + ", " + pickedUpOrder + "]";
	}
}
