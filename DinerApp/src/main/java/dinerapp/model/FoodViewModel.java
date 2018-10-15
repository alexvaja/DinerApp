package dinerapp.model;

public class FoodViewModel 
{
	private String foodName;
	private String ingredients;
	private int weight;
	private int price;
	
	public FoodViewModel()
	{
		super();
	}
	public FoodViewModel(String foodName, String ingredients, int weight, int price) {
		super();
		this.foodName = foodName;
		this.ingredients = ingredients;
		this.weight = weight;
		this.price = price;
	}
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public String getIngredients() {
		return ingredients;
	}
	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
}

