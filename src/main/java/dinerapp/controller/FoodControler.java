package dinerapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dinerapp.model.FoodViewModel;
import dinerapp.model.entity.Food;
import dinerapp.repository.FoodRepository;

@Controller
public class FoodControler {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private FoodRepository foodRepo;

	@GetMapping("/foodView")
	public String getAllFoods(final Model model) {
		LOGGER.info("getAllFoods");
		final FoodViewModel foodViewModel = new FoodViewModel();
		foodViewModel.setFoodItems(getListOfFood());
		model.addAttribute("foodViewModel", foodViewModel);
		return "views/foodView";
	}

	@PostMapping("/foodView")
	public String openFoodView(final Model model, @ModelAttribute final Food food,
			@RequestParam("submit") final String reqParam) {
		LOGGER.info("am intrat in mancare2");
		LOGGER.info(reqParam);
		Boolean addFoodIsAvailable = true;
		model.addAttribute("addFoodIsAvailable", addFoodIsAvailable);
		model.addAttribute("foodViewModel", new FoodViewModel(getListOfFood()));
		model.addAttribute("food", new Food());
		LOGGER.info(food.toString());
		switch (reqParam) {
		case "Add":
			addFoodIsAvailable = true;
			model.addAttribute("addFoodIsAvailable", addFoodIsAvailable);
			LOGGER.info(addFoodIsAvailable.toString());
			break;
		case "Save":
			foodRepo.save(food);
			addFoodIsAvailable = false;
			model.addAttribute("addFoodIsAvailable", addFoodIsAvailable);
			model.addAttribute("foodViewModel", new FoodViewModel(getListOfFood()));
			LOGGER.info(addFoodIsAvailable.toString());
			break;
		case "Cancel":
			addFoodIsAvailable = false;
			LOGGER.info(addFoodIsAvailable.toString());
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
		return searchedList;
	}
}
