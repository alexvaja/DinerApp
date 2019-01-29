package dinerapp.model;

import java.util.ArrayList;
import java.util.List;

import dinerapp.model.dto.OrderDTO;

public class UserReportViewModel {

//	private List<Order> orders;
//	private List<OrderQuantity> orderQuantities;
	private List<OrderDTO> listOfFoods;
	private List<String> dates;
	
	public UserReportViewModel() {
		super();
		this.dates = new ArrayList<>();
		this.listOfFoods = new ArrayList<>();
	}

	public UserReportViewModel(List<OrderDTO> listOfFoods, List<String> dates) {
		super();
		this.listOfFoods = listOfFoods;
		this.dates = dates;
	}

	public List<String> getDates() {
		return dates;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}

	public List<OrderDTO> getListOfFoods() {
		return listOfFoods;
	}

	public void setListOfFoods(List<OrderDTO> listOfFoods) {
		this.listOfFoods = listOfFoods;
	}

	@Override
	public String toString() {
		return "UserReportViewModel [" + listOfFoods + ", " + dates + "]";
	}
}
