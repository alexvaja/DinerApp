package dinerapp.controller;

import java.util.ArrayList;
import java.util.List;

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
public class FoodControler 
{
	@Autowired
	private FoodRepository foodRepo;
	@GetMapping("/foodView")
	public String getAllFoods(Model model) 
	{
		System.out.println("getAllFoods");
		FoodViewModel foodViewModel = new FoodViewModel();
		foodViewModel.setFoodItems(getListOfFood());
		model.addAttribute("foodViewModel", foodViewModel);
		return "foodView";
	}

	@PostMapping("/foodView")
	public String openFoodView(Model model, @ModelAttribute Food food, @RequestParam("submit") String reqParam)
	{
		System.out.println("am intrat in mancare2");
		System.out.println(reqParam);
		Boolean addFoodIsAvailable = true;
		model.addAttribute("addFoodIsAvailable", addFoodIsAvailable);
		model.addAttribute("foodViewModel", new FoodViewModel(getListOfFood()));
		model.addAttribute("food", new Food());
		System.out.println(food);
		switch (reqParam) 
		{
		case "Add":
			addFoodIsAvailable = true;
			model.addAttribute("addFoodIsAvailable", addFoodIsAvailable);
			System.out.println(addFoodIsAvailable);
			break;
		case "Save":
			foodRepo.save(food);
			addFoodIsAvailable = false;
			model.addAttribute("addFoodIsAvailable", addFoodIsAvailable);
			model.addAttribute("foodViewModel", new FoodViewModel(getListOfFood()));
			System.out.println(addFoodIsAvailable);
			break;
		case "Cancel":
			addFoodIsAvailable = false;
			System.out.println(addFoodIsAvailable);
			break;
		}
		return "foodView";
	}

	private List<Food> getListOfFood() 
	{
		Iterable<Food> list = foodRepo.findAll();
		List<Food> searchedList = new ArrayList<>();
		for (Food food : list)
			searchedList.add(food);
		return searchedList;
	}
}
