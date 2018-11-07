package dinerapp.model;

import java.util.ArrayList;
import java.util.List;

import dinerapp.entity.Category;
import dinerapp.entity.Food;
import dinerapp.entity.Menu;

public class MenuViewModel {

	private List<Category> categoryItems;
	private List<Food> foodItems;
	private List<Menu> menuItems;
	
	public MenuViewModel() {
		super();
		this.categoryItems = new ArrayList<>();
		this.foodItems = new ArrayList<>();
		this.menuItems = new ArrayList<>();
	}
	
	public MenuViewModel(List<Category> categoryItems, List<Food> foodItems, List<Menu> menuItems) {
		super();
		this.categoryItems = new ArrayList<>(categoryItems);
		this.foodItems = new ArrayList<>(foodItems);
		this.menuItems = new ArrayList<>(menuItems);
	}
	
	public List<Category> getCategoryItems() {
		return categoryItems;
	}
	
	public void setCategoryItems(List<Category> categoryItems) {
		this.categoryItems = categoryItems;
	}
	
	public List<Food> getFoodItems() {
		return foodItems;
	}
	
	public void setFoodItems(List<Food> foodItems) {
		this.foodItems = foodItems;
	}
	
	public List<Menu> getMenuItems() {
		return menuItems;
	}
	
	public void setMenuItems(List<Menu> menuItems) {
		this.menuItems = menuItems;
	}
	
	@Override
	public String toString() {
		return "MenuViewModel [categoryItems=" + categoryItems + ", foodItems=" + foodItems + ", menuItems=" + menuItems
				+ "]";
	}
}
