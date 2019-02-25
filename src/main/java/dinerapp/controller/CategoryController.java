package dinerapp.controller;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.itextpdf.text.log.SysoCounter;

import dinerapp.exceptions.NewSessionException;
import dinerapp.exceptions.WrongInputDataException;
import dinerapp.model.CategoryViewModel;
import dinerapp.model.FoodViewModel;
import dinerapp.model.dto.NewCategoryDTO;
import dinerapp.model.entity.Category;
import dinerapp.model.entity.Dish;
import dinerapp.model.entity.Menu;
import dinerapp.repository.CategoryRepository;
import dinerapp.repository.MenuRepository;

@Controller
public class CategoryController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	MenuRepository menuRepository;

	@ExceptionHandler({ NewSessionException.class })
	public String sessionError() {
		System.out.println("incercare de acces nepermis");
		return "views/loginView";
	}

	@ExceptionHandler({ WrongInputDataException.class })
	public String inputDataError() {
		System.out.println("date de intrare gresite");
		return "redirect:categoryView";
	}

	@GetMapping("/categoryView")
	public String getAllCateogires(Model model, HttpSession session) throws NewSessionException {

		LOGGER.info("getAllCategories");

		if (session.isNew()) {
			throw new NewSessionException();
		}

		model.addAttribute("categoryViewModel", new CategoryViewModel(getListOfCategory()));
		// model.addAttribute("category", new Category());
		LOGGER.info(getListOfCategory().toString());

		Boolean addCategoryIsAvailable = false;
		model.addAttribute("addCategoryIsAvailable", addCategoryIsAvailable);
		return "views/categoryView";
	}

	@PostMapping("/categoryView")
	public String setAllCategories(Model model, @ModelAttribute NewCategoryDTO newCategoryDTO,
			@RequestParam("submit") String reqParam,
			@RequestParam(value = "delete", required = false) String delCategory) throws WrongInputDataException {
		LOGGER.info("postAllCategories");

		Boolean addCategoryIsAvailable = false;
		model.addAttribute("addCategoryIsAvailable", addCategoryIsAvailable);
		model.addAttribute("categoryViewModel", new CategoryViewModel(getListOfCategory()));
		model.addAttribute("newCategoryDTO", new NewCategoryDTO());
		LOGGER.info(newCategoryDTO.toString());
		LOGGER.info(reqParam);

		switch (reqParam) {
		case "Adauga":
			addCategoryIsAvailable = true;
			model.addAttribute("addCategoryIsAvailable", addCategoryIsAvailable);
			break;
		case "Salveaza":

			Category category = new Category();

			if (newCategoryDTO.getName().isEmpty() || newCategoryDTO.getPrice().isEmpty()) {
				System.out.println("Datele nu sunt bune 1");
				throw new WrongInputDataException();
			} else {
				try {
					category.setName(newCategoryDTO.getName());
					category.setPrice(Double.parseDouble(newCategoryDTO.getPrice()));
					System.out.println("Datele sunt bune ");
				} catch (NumberFormatException e) {
					System.out.println("Datele nu sunt bune 2");
					throw new WrongInputDataException();
				}
			}
			categoryRepository.save(category);
			addCategoryIsAvailable = false;
			model.addAttribute("addCategoryIsAvailable", addCategoryIsAvailable);
			model.addAttribute("categoryViewModel", new CategoryViewModel(getListOfCategory()));

			break;
		case "Anuleaza":
			addCategoryIsAvailable = false;
			model.addAttribute("addCategoryIsAvailable", addCategoryIsAvailable);
			break;

		case "Sterge":
			addCategoryIsAvailable = false;
			if (delCategory != null) {
				List<String> foodIds = new ArrayList<>(Arrays.asList(delCategory.split(",")));
				Iterable<Menu> menuList = menuRepository.findAll();
				for (String item : foodIds) {
					int idInteger = Integer.parseInt(item);
					boolean categoryInMenu = false;
					for (Menu menu : menuList) {
						for (Dish dish : menu.getDishes()) {
							if (dish.getCategory().getId() == idInteger) {
								categoryInMenu = true;
							}
						}
					}
					if (categoryInMenu == false) {
						categoryRepository.deleteById(idInteger);
					} else {
						continue;
					}
				}
				model.addAttribute("categoryViewModel", new CategoryViewModel(getListOfCategory()));
				break;
			} else {
				break;
			}
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
