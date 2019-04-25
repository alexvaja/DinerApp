package junit;



import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import dinerapp.Application;
import dinerapp.controller.CategoryController;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@WebMvcTest(CategoryController.class)
public class WebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryController service;

    @MockBean
    private Model model;
    
    @MockBean
    private HttpSession session;

    @Test
    public void greetingShouldReturnMessageFromService() throws Exception {
        when(service.getAllCateogires(model, session)).thenReturn("views/categoryView");
        this.mockMvc.perform(get("/categoryView")).andDo(print()).andExpect(status().isOk());
               // .andExpect(content().string(containsString("Hello Mock")));
    }
}