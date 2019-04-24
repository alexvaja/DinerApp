package dinerapp.security.utils;

import java.util.Comparator;

import dinerapp.model.entity.Category;

public class CategoryComparer implements Comparator<Category>
{
	@Override
	public int compare(Category a, Category b) 
	{
		return a.getName().compareTo(b.getName());		
	}

}
