package dinerapp.model.dto;

import dinerapp.model.entity.Category;

public class CategoryDTO {

	private Category category;
	
	private Boolean selected;

	public CategoryDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CategoryDTO(Category category, Boolean selected) {
		super();
		this.category = category;
		this.selected = selected;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	@Override
	public String toString() {
		return "CategoryDTO [" + category + ", " + selected + "]";
	}
}
