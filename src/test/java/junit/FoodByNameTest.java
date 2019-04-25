package junit;

import static org.junit.Assert.*;

import java.util.Optional;

import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import dinerapp.model.entity.Food;
import dinerapp.repository.FoodRepository;


@RunWith(SpringRunner.class)
@DataJpaTest
public class FoodByNameTest {

	  @Autowired
	  private TestEntityManager entityManager;
	  
	  @Autowired
	  private FoodRepository foodRepository;
	  
	@Test
	public void test() {
		Food food = new Food("ciorba", "cartofi" , 300, 10.0);
		entityManager.persist(food);
		entityManager.flush();
		
		//when
		Optional<Food> found= foodRepository.findById(food.getId());
		
//		assertThat(found.get(), IsEqual<T>);
	}

}
