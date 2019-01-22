package dinerapp.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@Column(name = "id_order")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_food", nullable = false)
	private Food food;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_user", nullable = false)
	private UserDiner userDiner;
	
	@Column(name = "taken")
	private Boolean taken;

	@Column(name = "date", nullable = false)
	private String date;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name="order_food",
			joinColumns={@JoinColumn(name="id_order")},
			inverseJoinColumns={@JoinColumn(name="id_food")}
			)
	private List<Food> foodss = new ArrayList<>();

	public Order() {
		super();
	}

	public Order(Food food, UserDiner userDiner, Boolean taken, String date) {
		super();
		this.food = food;
		this.userDiner = userDiner;
		this.taken = taken;
		this.date = date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public UserDiner getUserDiner() {
		return userDiner;
	}

	public void setUserDiner(UserDiner userDiner) {
		this.userDiner = userDiner;
	}

	public Boolean getTaken() {
		return taken;
	}

	public void setTaken(Boolean taken) {
		this.taken = taken;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Food> getFoodss() {
		return foodss;
	}

	public void setFoodss(List<Food> foodss) {
		this.foodss = foodss;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Order [" + id + ", " + userDiner + ", " + taken + ", " + date + "]";
	}
}