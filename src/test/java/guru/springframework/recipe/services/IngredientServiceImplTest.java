package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.converters.Ingredient2IngredientCommand;
import guru.springframework.recipe.converters.Uom2UomCommand;
import guru.springframework.recipe.domain.Ingredient;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


public class IngredientServiceImplTest {

    private final Ingredient2IngredientCommand converter;

    @Mock
    RecipeRepository recipeRepository;

    IngredientService ingredientService;

    public IngredientServiceImplTest() {
        this.converter = new Ingredient2IngredientCommand(new Uom2UomCommand());
    }

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        ingredientService = new IngredientServiceImpl(recipeRepository, converter);
    }

    @Test
    public void findByIdsTest() {

        // given
        Ingredient i1 = Ingredient.builder().id(1L).build();
        Ingredient i2 = Ingredient.builder().id(2L).build();
        Ingredient i3 = Ingredient.builder().id(3L).build();

        Recipe recipe = Recipe.builder().id(1L).build();
        recipe.addIngredient(i1);
        recipe.addIngredient(i2);
        recipe.addIngredient(i3);

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        // when
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        // then
        IngredientCommand command = ingredientService.findByIds(1L, 3L);

        assertEquals(Long.valueOf(3L), command.getId());
        assertEquals(Long.valueOf(1L), command.getRecipeId());

        verify(recipeRepository, times(1)).findById(anyLong());
    }
}
