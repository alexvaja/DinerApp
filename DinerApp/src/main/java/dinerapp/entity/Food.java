package dinerapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="food_type")
public class Food 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_food_type")
	private Integer id;
	@Column(name="food_name")
	public String name;
	@Column(name="food_ingredients")
	public String ingredients;
	@Column(name="food_weight")
	public int weight;
	@Column(name="food_price")
	public int price;
	
	public Food() {
		super();
	}
	
	public Food(Integer id, String name, String ingredients, int weight, int price) {
		super();
		this.id = id;
		this.name = name;
		this.ingredients = ingredients;
		this.weight = weight;
		this.price = price;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "Food [id=" + id + ", name=" + name + ", ingredients=" + ingredients + ", weight=" + weight + ", price="
				+ price + "]";
	}
}