package dinerapp.model.dto;

import dinerapp.model.entity.UserDiner;

public class OrderDTO {
	
	private Integer id;
	private String date;
	private Boolean taken;
	private UserDiner userDiner;
	
	public OrderDTO() {
		super();
	}

	public OrderDTO(Integer id, String date, Boolean taken, UserDiner userDiner) {
		super();
		this.id = id;
		this.date = date;
		this.taken = taken;
		this.userDiner = userDiner;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Boolean getTaken() {
		return taken;
	}

	public void setTaken(Boolean taken) {
		this.taken = taken;
	}

	public UserDiner getUserDiner() {
		return userDiner;
	}

	public void setUserDiner(UserDiner userDiner) {
		this.userDiner = userDiner;
	}

	@Override
	public String toString() {
		return "OrderDTO [" + id + ", " + date + ", " + taken + ", " + userDiner + "]";
	}
}
