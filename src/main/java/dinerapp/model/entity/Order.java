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

import dinerapp.model.entity.UserDiner;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@Column(name = "id_order")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
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
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UserDiner getUser() {
		return userDiner;
	}

	public void setUser(UserDiner userDiner) {
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

	public List<Food> getFoods() {
		return foodss;
	}

	public void setFoods(List<Food> foods) {
		this.foodss = foods;
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
		return "Order [id=" + id + ", user=" + userDiner + ", taken=" + taken + ", date=" + date + "]";
	}
}