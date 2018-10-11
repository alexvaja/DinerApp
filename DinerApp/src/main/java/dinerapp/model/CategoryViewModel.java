package dinerapp.model;

import java.util.*;

public class CategoryViewModel {

	private List<Category> categoryItems = new ArrayList<Category>();

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
