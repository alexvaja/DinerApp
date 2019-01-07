package dinerapp.model;

import java.util.ArrayList;
import java.util.List;

import dinerapp.constants.MenuStates;
import dinerapp.model.dto.DishDTO;

public class MenuViewModel {

	private List<DishDTO> dishes;	
	private String title;	
	private String date;
	private String state;

	public MenuViewModel() {
		super();
		dishes = new ArrayList<>();
		state = MenuStates.NEW.toString();
	}
	
	public MenuViewModel(List<DishDTO> dishes, String title, String date, String state) {
		super();
		this.dishes = dishes;
		this.title = title;
		this.date = date;
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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
		return "MenuViewModel [" + dishes + ", " + title + ", " + date + ", " + state + "]";
	}	
}
