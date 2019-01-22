package dinerapp.repository;

import org.springframework.data.repository.CrudRepository;

import dinerapp.model.entity.UserDiner;

public interface UserCantinaRepository extends CrudRepository<UserDiner, Integer> {

}
