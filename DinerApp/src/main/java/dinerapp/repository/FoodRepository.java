package dinerapp.repository;

import org.springframework.data.repository.CrudRepository;

import dinerapp.model.entity.Food;

public interface FoodRepository extends CrudRepository<Food, Integer>
{

}
