package dinerapp.model;

public class Category {

	private String category;
	private Double price;
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "CategoryViewModel [category=" + category + ", price=" + price + "]";
	}
}