package dinerapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dinerapp.model.entity.Category;

@Repository 
public interface CategoryRepository extends CrudRepository<Category, Integer> {
//muie psd
}
