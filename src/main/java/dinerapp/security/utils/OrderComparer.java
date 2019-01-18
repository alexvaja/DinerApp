package dinerapp.security.utils;

import java.util.Comparator;
import dinerapp.model.entity.Order;

public class OrderComparer implements Comparator <Order>
{
	@Override
	public int compare (Order a, Order b)
	{
		return a.getDate().compareTo(b.getDate());
	}
}
