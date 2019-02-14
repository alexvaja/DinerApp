package dinerapp.model.dto;

public class NewFoodDTO {

	private String name;
	private String ingredients;
	private String weight;
	private String price;
	
	public NewFoodDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NewFoodDTO(String name, String ingredients, String weight, String price) {
		super();
		this.name = name;
		this.ingredients = ingredients;
		this.weight = weight;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "NewFoodDTO [" + name + ", " + ingredients + ", " + weight + ", " + price + "]";
	}
}
