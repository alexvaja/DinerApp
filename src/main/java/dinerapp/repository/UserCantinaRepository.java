package dinerapp.repository;

import org.springframework.data.repository.CrudRepository;

import dinerapp.model.entity.User;

public interface UserCantinaRepository extends CrudRepository<User, Integer> {

}
