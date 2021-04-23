package dinerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import dinerapp.model.entity.Menu;

@Component
@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

}
