package dinerapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dinerapp.exceptions.NewSessionException;
import dinerapp.model.CategoryViewModel;
import dinerapp.model.entity.Category;
import dinerapp.repository.CategoryRepository;

@Controller
public class CategoryController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CategoryRepository categoryRepository;
	
	@ExceptionHandler({ NewSessionException.class })
	public String sessionError() {
		System.out.println("incercare de acces nepermis");
		return "views/loginView";
	}

	@GetMapping("/categoryView")
	public String getAllCateogires(Model model, HttpSession session) throws NewSessionException {

		if (session.isNew()) {
			throw new NewSessionException();			
		}

		model.addAttribute("categoryViewModel", new CategoryViewModel(getListOfCategory()));
		// model.addAttribute("category", new Category());
		LOGGER.info(getListOfCategory().toString());
		return "views/categoryView";
	}

	@PostMapping("/categoryView")
	public String setAllCategories(Model model, @ModelAttribute Category category,
												@RequestParam("submit") String reqParam) {
		LOGGER.info("setAllCategories");

		Boolean addCategoryIsAvailable = false;
		model.addAttribute("addCategoryIsAvailable", addCategoryIsAvailable);
		model.addAttribute("categoryViewModel", new CategoryViewModel(getListOfCategory()));
		model.addAttribute("category", new Category());
		LOGGER.info(category.toString());
		LOGGER.info(reqParam);
		switch (reqParam) {
		case "Adauga":
			addCategoryIsAvailable = true;
			model.addAttribute("addCategoryIsAvailable", addCategoryIsAvailable);
			break;
		case "Salveaza":
			categoryRepository.save(category);
			addCategoryIsAvailable = false;
			model.addAttribute("addCategoryIsAvailable", addCategoryIsAvailable);
			model.addAttribute("categoryViewModel", new CategoryViewModel(getListOfCategory()));
			break;
		case "Anuleaza":
			addCategoryIsAvailable = false;
			model.addAttribute("addCategoryIsAvailable", addCategoryIsAvailable);
			break;
		}
		return "views/categoryView";
	}

	private List<Category> getListOfCategory() {

		final Iterable<Category> list = categoryRepository.findAll();
		final List<Category> searchedList = new ArrayList<>();
		for (final Category category : list) {
			searchedList.add(category);
		}
		return searchedList;
	}
}
