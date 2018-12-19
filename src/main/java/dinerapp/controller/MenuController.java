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

import dinerapp.model.MenuViewModel;
import dinerapp.model.entity.Category;
import dinerapp.model.entity.Dish;
import dinerapp.model.entity.Food;
import dinerapp.repository.CategoryRepository;
import dinerapp.repository.FoodRepository;

@Controller
public class MenuController {

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  FoodRepository foodRepository;

  int integer = 0;

  @GetMapping("/menuView")
  public String getAllMenu(final Model model) {

    LOGGER.info("GET MENU");
    LOGGER.info(getlistOfFood().toString());
    LOGGER.info(getListOfCategory().toString());

    final Boolean addMenuIsAvailable = false;
    model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
    model.addAttribute("menuViewModel", new MenuViewModel());

    return "menuView";
  }

  @PostMapping("/menuView")
  public String setAllMenu(final Model model, @RequestParam("submit") final String reqParam,
      @ModelAttribute("menuViewModel") final MenuViewModel menuViewModel) {

    LOGGER.info("SET MENU");
    LOGGER.info(reqParam);

    Boolean addMenuIsAvailable = false;
    model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
    model.addAttribute("categoryList", getListOfCategory());
    model.addAttribute("foodList", getlistOfFood());
    model.addAttribute("menuViewModel", menuViewModel);

    switch (reqParam) {
      case "AddMenu" :

        integer++;
        addMenuIsAvailable = true;
        model.addAttribute("addMenuIsAvailable", addMenuIsAvailable);
        LOGGER.info(menuViewModel.toString());
        menuViewModel.addNewDish(new Dish());
        model.addAttribute("menuViewModel", menuViewModel);
        break;
      case "Cancel" :
        menuViewModel.deleteAllElement();
        break;
      case "SaveAll" :
        break;
    }
    return "menuView";
  }

  private List<Category> getListOfCategory() {

    final Iterable<Category> list = categoryRepository.findAll();
    final List<Category> searchedList = new ArrayList<>();
    for (final Category category : list) {
      searchedList.add(category);
    }
    return searchedList;
  }

  private List<Food> getlistOfFood() {

    final Iterable<Food> list = foodRepository.findAll();
    final List<Food> searchedList = new ArrayList<>();
    for (final Food food : list) {
      searchedList.add(food);
    }
    return searchedList;
  }

  // private MenuViewModel getVM()
  // {
  // MenuViewModel menuViewModel = new MenuViewModel();
  // //Category
  // Category category1 = new Category(1, "Meniu 1", 17.);
  //
  // Category category2 = new Category(2, "Meniu 2", 19.);
  //
  // Category category3 = new Category(3, "Meniu Vegetarian", 20.5);
  // //Food
  // Food food1 = new Food(5, "Ciorba", "apa, sare, piper, morcovi", 250, 6);
  //
  // Food food2 = new Food(6, "Supa", "apa, sare, piper, morcovi", 300, 5);
  //
  // Food food3 = new Food(7, "Tocanita", "apa, sare, piper, morcovi", 260, 10);
  //
  // Food food4 = new Food(8, "Gulas", "apa, sare, piper, morcovi", 600, 12);
  // //Dish
  // Dish dish1 = new Dish();
  // dish1.setCategory(category1);
  // dish1.addNewFood(food1);
  // dish1.addNewFood(food3);
  // dish1.addNewFood(food4);
  //
  // Dish dish2 = new Dish();
  // dish2.setCategory(category2);
  // dish2.addNewFood(food1);
  // dish2.addNewFood(food3);
  // dish2.addNewFood(food4);
  //
  // Dish dish3 = new Dish();
  // dish3.setCategory(category3);
  // dish3.addNewFood(food1);
  // dish3.addNewFood(food2);
  // dish3.addNewFood(food3);
  // dish3.addNewFood(food4);
  //
  // menuViewModel.addNewDish(dish1);
  // menuViewModel.addNewDish(dish2);
  // menuViewModel.addNewDish(dish3);
  // menuViewModel.addNewDish(dish1);
  // menuViewModel.addNewDish(dish2);
  //
  // return menuViewModel;
  // }
}
