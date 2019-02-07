package dinerapp.model;

import java.util.ArrayList;
import java.util.List;

import dinerapp.model.entity.Menu;

public class EditMenuViewModel {

	private List<Menu> menus;

	public EditMenuViewModel() {
		super();
		this.menus = new ArrayList<>();
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	@Override
	public String toString() {
		return "EditMenuViewModel [" + menus + "]";
	}
}