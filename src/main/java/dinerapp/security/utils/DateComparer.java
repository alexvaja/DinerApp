package dinerapp.security.utils;

import java.util.Comparator;
import dinerapp.model.entity.Menu;

public class DateComparer implements Comparator <Menu> 
{
	@Override
	public int compare(Menu a, Menu b)
	{
		return a.getDate().compareTo(b.getDate());
	}
}
