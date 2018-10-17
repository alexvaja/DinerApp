package dinerapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import dinerapp.entity.Food;
import dinerapp.model.FoodViewModel;
import dinerapp.repository.FoodRepository;

@Controller
public class FoodControler 
{
	@Autowired
	private FoodRepository foodRepo;

	@GetMapping("/foodView")
	public String openFoodView(Model model) {
		System.out.println("am intrat in mancare");
		model.addAttribute("foodViewModel", new FoodViewModel());
		return "foodView";
	}

	@PostMapping("/foodView")
	public String openFoodView(@ModelAttribute FoodViewModel foodViewModel) {
		System.out.println("am intrat in mancare2");
		Food food = new Food();
		food.setFoodName(foodViewModel.getFoodName());
		food.setIngredients(foodViewModel.getIngredients());
		food.setWeight(foodViewModel.getWeight());
		food.setPrice(foodViewModel.getPrice());
		foodRepo.save(food);
		return "foodView";
	}
}
