package dinerapp.repository;

import org.springframework.data.repository.CrudRepository;

import dinerapp.model.entity.OrderQuantity;

public interface OrderQuantityRepository extends CrudRepository<OrderQuantity,Integer> 
{
	
}
