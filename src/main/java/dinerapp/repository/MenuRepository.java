package dinerapp.repository;

import org.springframework.data.repository.CrudRepository;

import dinerapp.model.entity.Menu;

public interface MenuRepository extends CrudRepository<Menu, Integer> {

}
