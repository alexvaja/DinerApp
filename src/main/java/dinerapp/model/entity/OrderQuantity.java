package dinerapp.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="order_quantity")
public class OrderQuantity {

	@Id
	@Column(name="id_quantity")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "id_order", nullable = false)
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "id_food", nullable = false)
	private Food food;
	
	@Column(name="quantity")
	private Integer quantity;

	public OrderQuantity() {
		super();
	}

	public OrderQuantity(Integer id, Order order, Food food, Integer quantity) {
		super();
		this.id = id;
		this.order = order;
		this.food = food;
		this.quantity = quantity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "OrderQuantity [" + id + ", " + order + ", " + food + ", " + quantity + "]";
	}
}
