package dinerapp.model;

import java.util.ArrayList;
import java.util.List;
import dinerapp.entity.Food;

public class FoodViewModel 
{
	private List<Food> foodItems;

	public FoodViewModel() {
		super();
		this.foodItems = new ArrayList<>();
	}
	
	public FoodViewModel(List<Food> foodItems) {
		super();
		this.foodItems = new ArrayList<>(foodItems);
	}
	
	public List<Food> getFoodItems() {
		return foodItems;
	}

	public void setFoodItems(List<Food> foodItems) {
		this.foodItems = foodItems;
	}
	
	public void addFood(Food food)
	{
		this.foodItems.add(food);
	}

	@Override
	public String toString() {
		return "FoodViewModel [foodItems=" + foodItems + "]";
	}
}