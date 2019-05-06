package dinerapp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import dinerapp.exceptions.WrongInputDataException;
import dinerapp.model.FoodViewModel;
import dinerapp.model.dto.NewFoodDTO;
import dinerapp.model.entity.Dish;
import dinerapp.model.entity.Food;
import dinerapp.model.entity.Menu;
import dinerapp.repository.FoodRepository;
import dinerapp.repository.MenuRepository;
import dinerapp.security.utils.FoodComparer;

@Controller
public class FoodControler {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FoodRepository foodRepo;

	@Autowired
	MenuRepository menuRepository;

	@ExceptionHandler({ NewSessionException.class })
	public String sessionError() {
		LOGGER.error("incercare de acces nepermis");
		return "views/loginView";
	}

	@ExceptionHandler({ WrongInputDataException.class })
	public String inputDataError() {
		LOGGER.error("date de intrare gresite");
		return "redirect:foodView";
	}

	@GetMapping("/foodView")
	public String getAllFoods(Model model, HttpSession session) throws NewSessionException {

		if (session.isNew()) {
			throw new NewSessionException();
		}

		LOGGER.info("getAllFoods");
		FoodViewModel foodViewModel = new FoodViewModel();
		foodViewModel.setFoodItems(getListOfFood());
		model.addAttribute("foodViewModel", foodViewModel);

		Boolean addFoodIsAvailable = false;
		model.addAttribute("addFoodIsAvailable", addFoodIsAvailable);
		return "views/foodView";
	}

	@PostMapping("/foodView")
	public String openFoodView(Model model, @ModelAttribute NewFoodDTO newFoodDTO,
			@RequestParam(value = "forDelete", required = false) String foodsIdForDelete,
			@RequestParam("submit") String reqParam) throws WrongInputDataException {
		
		LOGGER.info(foodsIdForDelete + " ");
		LOGGER.info(reqParam);
		
		Boolean addFoodIsAvailable = false;
		model.addAttribute("addFoodIsAvailable", addFoodIsAvailable);
		model.addAttribute("foodViewModel", new FoodViewModel(getListOfFood()));
		model.addAttribute("newFoodDTO", new NewFoodDTO());
		LOGGER.info(newFoodDTO.toString());

		switch (reqParam) {
		case "Adauga":
			addFoodIsAvailable = true;
			model.addAttribute("addFoodIsAvailable", addFoodIsAvailable);
			LOGGER.info(addFoodIsAvailable.toString());
			break;

		case "Salveaza":
			LOGGER.info(addFoodIsAvailable.toString());
			Food food = new Food();
			
			
			try {
				Double price = Double.parseDouble(newFoodDTO.getPrice());
				Integer weight = Integer.parseInt(newFoodDTO.getWeight());
				
			} catch (NumberFormatException e) {
				throw new WrongInputDataException();
			}
			
			

			if (newFoodDTO.getName().isEmpty() || newFoodDTO.getIngredients().isEmpty()
					|| newFoodDTO.getPrice().isEmpty() || Double.parseDouble(newFoodDTO.getPrice()) < 0 
					|| newFoodDTO.getWeight().isEmpty() || Integer.parseInt(newFoodDTO.getWeight()) < 0) {
				throw new WrongInputDataException();
			} else {
				try {
					food.setName(newFoodDTO.getName());
					food.setIngredients(newFoodDTO.getIngredients());				
					food.setPrice(Double.parseDouble(newFoodDTO.getPrice()));
					food.setWeight(Integer.parseInt(newFoodDTO.getWeight()));
				} catch (NumberFormatException e) {
					throw new WrongInputDataException();
				}
			}

			foodRepo.save(food);
			addFoodIsAvailable = false;
			model.addAttribute("addFoodIsAvailable", addFoodIsAvailable);
			model.addAttribute("foodViewModel", new FoodViewModel(getListOfFood()));
			break;

		case "Anuleaza":
			addFoodIsAvailable = false;
			model.addAttribute("addFoodIsAvailable", addFoodIsAvailable);
			LOGGER.info(addFoodIsAvailable.toString());
			break;

		case "Sterge":
			addFoodIsAvailable = false;
			if (foodsIdForDelete != null) {
				List<String> foodIds = new ArrayList<>(Arrays.asList(foodsIdForDelete.split(",")));
				Iterable<Menu> menuList = menuRepository.findAll();

				for (String item : foodIds) {
					int idInteger = Integer.parseInt(item);
					boolean foodInMenu = false;
					
					for (Menu menu : menuList) {
						for (Dish dish : menu.getDishes()) {
							for (Food foodId : dish.getFoods()) {
								if (foodId.getId() == idInteger)
									foodInMenu = true;
							}
						}

					}
					if (foodInMenu == false) {
						foodRepo.deleteById(idInteger);
					} else {
						continue;
					}

				}
			} else {
				break;
			}
			model.addAttribute("addFoodIsAvailable", addFoodIsAvailable);
			model.addAttribute("foodViewModel", new FoodViewModel(getListOfFood()));
			break;
		}
		return "views/foodView";
	}

	private List<Food> getListOfFood() {
		final Iterable<Food> list = foodRepo.findAll();
		final List<Food> searchedList = new ArrayList<>();
		for (final Food food : list) {
			searchedList.add(food);
		}
		sortFoodsByName(searchedList);
		return searchedList;
	}
	
	private void sortFoodsByName(List<Food> foods)
	{
		Collections.sort(foods, new FoodComparer());
	}
}
