package dinerapp.model;

import java.util.ArrayList;
import java.util.List;

import dinerapp.dto.DishDTO;

public class MenuViewModel {

	private List<DishDTO> dishes;
	
	private String title;
	
	private String date;

	public MenuViewModel() {
		super();
		dishes = new ArrayList<>();
	}
	
	public MenuViewModel(List<DishDTO> dishes, String title, String date) {
		super();
		this.dishes = dishes;
		this.title = title;
		this.date = date;
	}

	public List<DishDTO> getDishes() {
		return dishes;
	}

	public void setDishes(List<DishDTO> dishes) {
		this.dishes = dishes;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "MenuViewModel [" + dishes + ", " + title + ", " + date + "]";
	}	
}
