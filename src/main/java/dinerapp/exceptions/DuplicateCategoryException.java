package dinerapp.exceptions;

public class DuplicateCategoryException extends Exception{

	private static final long serialVersionUID = 6060535839967409813L;
	
	public DuplicateCategoryException(String message) {
		super(message);
	}
}