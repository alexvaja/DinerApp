package dinerapp.model;

import java.util.ArrayList;
import java.util.List;

import dinerapp.entity.Category;

public class CategoryViewModel {

	private List<Category> categoryItems = new ArrayList<Category>();
	
	public CategoryViewModel() {
		super();
		categoryItems = new ArrayList<>();
	}

	public List<Category> getCategoryItems() {
		return categoryItems;
	}

	public void setCategoryItems(List<Category> categoryItems) {
		this.categoryItems = categoryItems;
	}

	@Override
	public String toString() {
		return "CategoryViewModel [categoryItems=" + categoryItems + "]";
	}
}
