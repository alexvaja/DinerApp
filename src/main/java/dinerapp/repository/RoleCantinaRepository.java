package dinerapp.repository;

import org.springframework.data.repository.CrudRepository;

import dinerapp.model.entity.Role;

public interface RoleCantinaRepository extends CrudRepository<Role, Integer> {

}
