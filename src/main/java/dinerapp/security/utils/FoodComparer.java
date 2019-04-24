package dinerapp.security.utils;

import java.util.Comparator;

import dinerapp.model.entity.Food;

public class FoodComparer implements Comparator <Food>
{
	@Override
	public int compare(Food a, Food b) 
	{
		return a.getName().compareTo(b.getName());
	}
}
