package dinerapp.dto;

import dinerapp.model.entity.Food;

public class FoodDTO {

	private Food food;
	
	private boolean selected;

	public FoodDTO() {
		super();
	}
	
	public FoodDTO(Food food, boolean selected) {
		super();
		this.food = food;
		this.selected = selected;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public String toString() {
		return "FoodDTO [" + food + ", " + selected + "]";
	}
}
