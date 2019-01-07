package dinerapp.model;

import java.util.ArrayList;
import java.util.List;

import dinerapp.model.dto.DishDTO;
import dinerapp.model.dto.MenuDTO;

public class MenuViewModel {

	private List<DishDTO> dishes;	
	private MenuDTO menu;

	public MenuViewModel() {
		super();
		dishes = new ArrayList<>();
		menu = new MenuDTO();
	}

	public MenuViewModel(List<DishDTO> dishes, MenuDTO menu) {
		super();
		this.dishes = dishes;
		this.menu = menu;
	}

	public List<DishDTO> getDishes() {
		return dishes;
	}

	public void setDishes(List<DishDTO> dishes) {
		this.dishes = dishes;
	}

	public MenuDTO getMenu() {
		return menu;
	}

	public void setMenu(MenuDTO menu) {
		this.menu = menu;
	}

	@Override
	public String toString() {
		return "MenuViewModel [" + dishes + ", " + menu + "]";
	}
}