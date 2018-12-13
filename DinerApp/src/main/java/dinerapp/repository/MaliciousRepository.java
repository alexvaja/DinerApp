package dinerapp.repository;

import org.springframework.data.repository.CrudRepository;

import dinerapp.entity.Malicious;

public interface MaliciousRepository extends CrudRepository<Malicious, Integer> {

}
