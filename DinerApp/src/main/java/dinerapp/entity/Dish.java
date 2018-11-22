package dinerapp.entity;

import java.util.ArrayList;
import java.util.List;

public class Dish {

	private Category category;
	private List<Food> foodItems;
	
	public Dish() {
		super();
		this.category = null;
		this.foodItems = new ArrayList<>();
	}

	public Dish(Category category, List<Food> foodItems) {
		super();
		this.category = category;
		this.foodItems = new ArrayList<>(foodItems);
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public List<Food> getFoodItems() {
		return foodItems;
	}
	
	public void setFoodItems(List<Food> foodItems) {
		this.foodItems = foodItems;
	}

	@Override
	public String toString() {
		return "Dish [category=" + category + ", foodItems=" + foodItems + "]";
	}
}
