package dinerapp.model.dto;

import java.util.ArrayList;
import java.util.List;

public class DishDTO {
	
	private Integer id;
	private List<CategoryDTO> categories;
	private List<FoodDTO> foods;

	public DishDTO() {
		super();
		this.categories = new ArrayList<>();
		this.foods = new ArrayList<>();
	}

	public DishDTO(Integer id, List<CategoryDTO> categories, List<FoodDTO> foods) {
		super();
		this.id = id;
		this.categories = categories;
		this.foods = foods;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		return "DishDTO [" + id + ", " + categories + ", " + foods + "]";
	}
}