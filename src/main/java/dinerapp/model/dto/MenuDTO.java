package dinerapp.model.dto;

import dinerapp.constants.MenuStates;

public class MenuDTO {

	private Integer id;
	private String date;
	private String state;
	private String title;
	
	public MenuDTO() {
		super();
		this.state = MenuStates.NEW.toString();
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "MeniuDTO [" + id + ", " + date + ", " + state + ", " + title + "]";
	}
}
