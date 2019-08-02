package guru.springframework.recipe.controllers;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.services.IngredientService;
import guru.springframework.recipe.services.RecipeService;
import guru.springframework.recipe.services.UomService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IngredientControllerTest {

    MockMvc mockMvc;

    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    @Mock
    UomService uomService;

    IngredientController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        controller = new IngredientController(recipeService, ingredientService, uomService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testListIngredients() throws Exception {

        // given
        RecipeCommand recipeCommand = new RecipeCommand();
        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        // when
        mockMvc.perform(get("/recipe/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));

        // then
        verify(recipeService, times(1)).findCommandById(anyLong());
    }

    @Test
    public void testShowIngredient() throws Exception {

        // given
        IngredientCommand ingredientCommand = new IngredientCommand();

        // when
        when(ingredientService.findByIds(anyLong(), anyLong())).thenReturn(ingredientCommand);

        // then
        mockMvc.perform(get("/recipe/1/ingredient/2/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));
    }

    @Test
    public void testUpdateIngredientForm() throws Exception {

        // given
        IngredientCommand command = new IngredientCommand();

        // when
        when(ingredientService.findByIds(anyLong(), anyLong())).thenReturn(command);
        when(uomService.listAllUoms()).thenReturn(Collections.emptySet());

        // then
        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient", "uomList"));
    }

    @Test
    public void testSaveOrUpdate() throws Exception {

        // given
        IngredientCommand command = IngredientCommand.builder().id(3L).recipeId(2L).build();

        // when
        when(ingredientService.saveIngredientCommand(any())).thenReturn(command);

        // then
        mockMvc.perform(post("/recipe/2/ingredient")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("id", "")
                    .param("description", "some string"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/ingredient/3/show"));
    }

    @Test
    public void testNewIngredientForm() throws Exception {

        // given
        RecipeCommand command = RecipeCommand.builder().id(1L).build();

        // when
        when(recipeService.findCommandById(anyLong())).thenReturn(command);
        when(uomService.listAllUoms()).thenReturn(Collections.emptySet());

        // then
        mockMvc.perform(get("/recipe/1/ingredient/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient", "uomList"));

        verify(recipeService, times(1)).findCommandById(anyLong());
    }

    @Test
    public void testDeleteAction() throws Exception {

        mockMvc.perform(get("/recipe/1/ingredient/3/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/ingredients"));

        verify(ingredientService, times(1)).deleteByIds(anyLong(), anyLong());
    }
}