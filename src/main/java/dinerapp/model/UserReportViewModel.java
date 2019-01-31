package dinerapp.model;

import java.util.ArrayList;
import java.util.List;

import dinerapp.model.dto.OrderDTO;

public class UserReportViewModel {

	private List<OrderDTO> listOfFoods;
	private String date;
	
	public UserReportViewModel() {
		super();
		this.listOfFoods = new ArrayList<>();
	}

	public UserReportViewModel(List<OrderDTO> listOfFoods, List<String> dates) {
		super();
		this.listOfFoods = listOfFoods;
	}

	public List<OrderDTO> getListOfFoods() {
		return listOfFoods;
	}

	public void setListOfFoods(List<OrderDTO> listOfFoods) {
		this.listOfFoods = listOfFoods;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "UserReportViewModel [" + listOfFoods + ", " + date + "]";
	}
}
