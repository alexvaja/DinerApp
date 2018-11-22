package dinerapp.model;

import java.util.ArrayList;
import java.util.List;

import dinerapp.entity.Dish;

public class MenuViewModel {

	List<Dish> dishItems;

	public MenuViewModel() {
		super();
		this.dishItems = new ArrayList<>();
	}

	public MenuViewModel(List<Dish> dishItems) {
		super();
		this.dishItems = new ArrayList<>(dishItems);
	}

	public List<Dish> getDishItems() {
		return dishItems;
	}

	public void setDishItems(List<Dish> dishItems) {
		this.dishItems = dishItems;
	}

	@Override
	public String toString() {
		return "MenuViewModel [dishItems=" + dishItems + "]";
	}
}
