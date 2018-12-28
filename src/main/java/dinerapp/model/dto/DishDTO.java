package dinerapp.model.dto;

import java.util.ArrayList;
import java.util.List;

public class DishDTO {

	private List<CategoryDTO> categories;
	
	private List<FoodDTO> foods;

	public DishDTO() {
		super();
		this.categories = new ArrayList<>();
		this.foods = new ArrayList<>();
	}

	public DishDTO(List<CategoryDTO> categories, List<FoodDTO> foods) {
		super();
		this.categories = categories;
		this.foods = foods;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryDTO> categories) {
		this.categories = categories;
	}

	public List<FoodDTO> getFoods() {
		return foods;
	}

	public void setFoods(List<FoodDTO> foods) {
		this.foods = foods;
	}

	@Override
	public String toString() {
		return "DishDTO [" + categories + ", " + foods + "]";
	}
}
