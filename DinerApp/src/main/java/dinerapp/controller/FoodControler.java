package dinerapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import dinerapp.entity.Food;
import dinerapp.model.FoodViewModel;
import dinerapp.repository.FoodRepository;

@Controller
public class FoodControler 
{
	@Autowired
	private FoodRepository foodRepo;

	@GetMapping("/allFoods")
	public String getAllFoods(Model model) {
		System.out.println("getAllFoods");
		
		model.addAttribute("foodViewModel", new FoodViewModel());
		return "allFoods";
	}

//	@PostMapping("/foodView")
//	public String openFoodView(@ModelAttribute FoodViewModel foodViewModel) {
//		System.out.println("am intrat in mancare2");
//		Food food = new Food();
//		food.setFoodName(foodViewModel.getFoodName());
//		food.setIngredients(foodViewModel.getIngredients());
//		food.setWeight(foodViewModel.getWeight());
//		food.setPrice(foodViewModel.getPrice());
//		foodRepo.save(food);
//		return "foodView";
//	}
}
