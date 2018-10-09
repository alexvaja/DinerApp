package dinerapp.repository;

import org.springframework.data.repository.CrudRepository;
import dinerapp.entity.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

}
