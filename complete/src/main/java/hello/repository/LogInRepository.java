package hello.repository;

import org.springframework.data.repository.CrudRepository;

import hello.entity.User;

public interface LogInRepository extends CrudRepository<User, Integer>  {
	

}