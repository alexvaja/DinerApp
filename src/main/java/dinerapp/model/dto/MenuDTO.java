package dinerapp.model.dto;

public class MenuDTO {

	private Integer id;
	private String date;
	private String state;
	private String Title;
	
	public MenuDTO() {
		super();
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
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	@Override
	public String toString() {
		return "MeniuDTO [" + id + ", " + date + ", " + state + ", " + Title + "]";
	}
}
