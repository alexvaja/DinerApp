package dinerapp.repository;

import org.springframework.data.repository.CrudRepository;

import dinerapp.entity.Food;

public interface FoodRepository extends CrudRepository<Food, Integer>
{

}
