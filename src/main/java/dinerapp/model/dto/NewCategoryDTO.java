package dinerapp.model.dto;

public class NewCategoryDTO {
	
	private String name;
	private String price;
	
	public NewCategoryDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NewCategoryDTO(String name, String price) {
		super();
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "NewCategoryDTO [" + name + ", " + price + "]";
	}
}
