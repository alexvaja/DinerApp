package dinerapp.model;
    
import java.util.ArrayList;
import java.util.List;   
import dinerapp.model.entity.UserDiner;
    
public class StatisticsViewModel 
{   
	private List<UserDiner>users;
	private List<Integer> placedOrders;
	private List<Integer> pickedUpOrders;
	
	public StatisticsViewModel() 
	{
		super();
		this.users = new ArrayList<>();
		this.placedOrders = new ArrayList<>();
		this.pickedUpOrders = new ArrayList<>();
	}
	public List<UserDiner> getUsers() {
		return users;
	}
	public void setUsers(List<UserDiner> users) {
		this.users = users;
	}
	public List<Integer> getPlacedOrders() {
		return placedOrders;
	}
	public void setPlacedOrders(List<Integer> placedOrders) {
		this.placedOrders = placedOrders;
	}
	public List<Integer> getPickedUpOrders() {
		return pickedUpOrders;
	}
	public void setPickedUpOrders(List<Integer> pickedUpOrders) {
		this.pickedUpOrders = pickedUpOrders;
	}
	
}