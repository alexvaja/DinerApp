package dinerapp.security.utils;

public class NormalizeText 
{
	public static String normalizeString(String string)
	{
		String lowerString = string.toLowerCase();
		String result = lowerString.substring(0,1).toUpperCase() + lowerString.substring(1);
		
		return result;
	}
}
