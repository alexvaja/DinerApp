package dinerapp.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.openjdk.jmh.annotations.Setup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import dinerapp.controller.CategoryController;
import dinerapp.exceptions.DuplicateCategoryException;
import dinerapp.exceptions.NewSessionException;
import dinerapp.exceptions.WrongInputDataException;
import dinerapp.model.CategoryViewModel;
import dinerapp.model.dto.NewCategoryDTO;
import dinerapp.model.entity.Category;
import dinerapp.repository.CategoryRepository;

public class CategoryControllerTest {
	
//	@Autowired
//	private CategoryRepository categoryRepository;
	
	
	@Test
	public void test() throws NewSessionException {
	
		
		CategoryViewModel categoryViewModelMock = new CategoryViewModel();
		
		Category anotherCategory = mock(Category.class);
		anotherCategory.setId(1);
		anotherCategory.setName("testing");
		anotherCategory.setPrice(100.0);
		
		categoryViewModelMock.addCategory(anotherCategory);
		
		CategoryController categoryController = new CategoryController();
		
		
		Model model = mock(Model.class);
		model.addAttribute("categoryViewModel", categoryViewModelMock);
		HttpSession session = mock(HttpSession.class);
		NewCategoryDTO newCategoryDTO = mock(NewCategoryDTO.class);

		try {
			categoryController.setAllCategories(model, newCategoryDTO, "adauga", "delete");
		} catch (WrongInputDataException | DuplicateCategoryException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String filteredCategory = categoryController.getAllCateogires(model, session);

		assertEquals("views/categoryView", filteredCategory);
	}

}
