package dinerapp.repository;

import org.springframework.data.repository.CrudRepository;

import dinerapp.model.entity.Dish;

public interface DishRepository extends CrudRepository<Dish, Integer> {

}
