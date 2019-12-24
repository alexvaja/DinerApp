package dinerapp.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dinerapp.constants.MenuStates;
import dinerapp.model.entity.Menu;
import dinerapp.model.entity.Role;
import dinerapp.model.entity.UserDiner;
import dinerapp.repository.MenuRepository;
import dinerapp.repository.UserCantinaRepository;
import dinerapp.security.utils.DateComparer;

@Controller
public class IndexController {
	@Autowired
	private MenuRepository menuRepository;
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserCantinaRepository userRepository;

	@GetMapping("index")
	public String getIndex(Model model, HttpSession session, Principal principal) {
		//
		if (principal != null) {
			UserDiner user = userRepository.findByName(principal.getName());
			Role userRole = user.getRoles().get(0);
			
			if (userRole.getName().equals("ROLE_USER")) {
				return "views/employeeOrderView";
			}
		}
		//
		model.addAttribute("menusList", getOnlyPublishedMenu(getMenuFromTable()));
		Boolean viewMenu = false;
		model.addAttribute("viewMenu", viewMenu);

		return "index";
	}

	@PostMapping("index")
	public String sertIndex(Model model, HttpSession session, @RequestParam MultiValueMap<String, String> params) {
		Boolean viewMenu = false;
		model.addAttribute("viewMenu", viewMenu);
		model.addAttribute("menusList", getOnlyPublishedMenu(getMenuFromTable()));

		String idMenu = null;

		for (String key : params.keySet()) {
			idMenu = key;
		}

		String reqParam = params.getFirst(idMenu);
		LOGGER.info(reqParam);
		System.out.println(reqParam);

		switch (reqParam) {
		case "Inapoi": {

			viewMenu = false;
			model.addAttribute("viewMenu", viewMenu);
			break;
		}
		case "Vezi": {

			LOGGER.info(idMenu);
			Menu searchedMenu = null;

			List<Menu> menuList = getMenuFromTable();
			for (Menu menu : menuList) {
				if (menu.getId() == Integer.parseInt(idMenu)) {
					searchedMenu = menu;
				}
			}

			model.addAttribute("searchedMenu", searchedMenu);

			viewMenu = true;
			model.addAttribute("viewMenu", viewMenu);
			break;
		}
		}

		return "index";
	}

	private List<Menu> getMenuFromTable() {
		Iterable<Menu> searchedList = menuRepository.findAll();
		List<Menu> menusList = new ArrayList<>();

		for (Menu menu : searchedList) {
			menusList.add(menu);
		}
		return menusList;
	}

	private List<Menu> getOnlyPublishedMenu(List<Menu> menus) {

		List<Menu> publishedMenu = new ArrayList<Menu>();
		for (Menu menu : menus) {
			if (menu.getState().equals(MenuStates.PUBLISHED.toString())) {
				publishedMenu.add(menu);
			}
		}
		System.out.println("Lista Meniu" + publishedMenu);
		sortMenus(publishedMenu);
		return publishedMenu;
	}

	private void sortMenus(List<Menu> menus) {
		Collections.sort(menus, new DateComparer());
	}
}
