package dinerapp.model;

import java.util.ArrayList;
import java.util.List;

import dinerapp.model.dto.DishDTO;
import dinerapp.model.dto.MenuDTO;

public class MenuViewModel {

	private List<DishDTO> dishesDTO;	
	private MenuDTO menuDTO;

	public MenuViewModel() {
		super();
		dishesDTO = new ArrayList<>();
		menuDTO = new MenuDTO();
	}

	public List<DishDTO> getDishesDTO() {
		return dishesDTO;
	}

	public void setDishesDTO(List<DishDTO> dishesDTO) {
		this.dishesDTO = dishesDTO;
	}

	public MenuDTO getMenuDTO() {
		return menuDTO;
	}

	public void setMenuDTO(MenuDTO menuDTO) {
		this.menuDTO = menuDTO;
	}

	@Override
	public String toString() {
		return "MenuViewModel [" + dishesDTO + ", " + menuDTO + "]";
	}
}