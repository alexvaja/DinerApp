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
import dinerapp.exceptions.WrongInputDataException;
import dinerapp.model.FoodViewModel;
import dinerapp.model.dto.NewFoodDTO;
import dinerapp.model.entity.Food;
import dinerapp.repository.FoodRepository;

@Controller
public class FoodControler {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FoodRepository foodRepo;
	
	@ExceptionHandler({ NewSessionException.class })
	public String sessionError() {
		System.out.println("incercare de acces nepermis");
		return "views/loginView";
	}

	@ExceptionHandler({ WrongInputDataException.class })
	public String inputDataError() {
		System.out.println("date de intrare gresite");
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
											@RequestParam("submit") String reqParam) throws WrongInputDataException {
		
		LOGGER.info("am intrat in mancare2");
		LOGGER.info(reqParam);
		Boolean addFoodIsAvailable = true;
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
			
			if (newFoodDTO.getName().isEmpty() || newFoodDTO.getIngredients().isEmpty() || newFoodDTO.getPrice().isEmpty() || newFoodDTO.getWeight().isEmpty()) {
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
