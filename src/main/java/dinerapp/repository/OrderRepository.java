package dinerapp.repository;

import org.springframework.data.repository.CrudRepository;
import dinerapp.model.entity.Order;

public interface OrderRepository extends CrudRepository <Order, Integer>{

}
