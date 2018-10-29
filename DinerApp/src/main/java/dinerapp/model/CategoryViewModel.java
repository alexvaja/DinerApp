package dinerapp.model;

import java.util.ArrayList;
import java.util.List;

import dinerapp.entity.Category;

public class CategoryViewModel {

	private List<Category> categoryItems;
	
	public CategoryViewModel() {
		super();
		this.categoryItems = new ArrayList<>();
	}

	public CategoryViewModel(List<Category> categoryItems) {
		super();
		this.categoryItems = new ArrayList<>(categoryItems);
	}
	
	public void addCategory(Category anotherCategory) {
		categoryItems.add(anotherCategory);
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
