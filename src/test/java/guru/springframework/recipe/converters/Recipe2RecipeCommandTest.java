package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.domain.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class Recipe2RecipeCommandTest {

    public static final Long RECIPE_ID = 1L;
    public static final Integer COOK_TIME = Integer.valueOf("5");
    public static final Integer PREP_TIME = Integer.valueOf("7");
    public static final String DESCRIPTION = "My Recipe";
    public static final String DIRECTIONS = "Directions";
    public static final Difficulty DIFFICULTY = Difficulty.EASY;
    public static final Integer SERVINGS = Integer.valueOf("3");
    public static final String SOURCE = "Source";
    public static final String URL = "Some URL";
    public static final Long CAT_ID_1 = 1L;
    public static final Long CAT_ID_2 = 2L;
    public static final Long INGRED_ID_1 = 3L;
    public static final Long INGRED_ID_2 = 4L;
    public static final Long NOTES_ID = 9L;

    Recipe2RecipeCommand converter;

    @Before
    public void setUp() throws Exception {
        converter = new Recipe2RecipeCommand(new Category2CategoryCommand(), new Ingredient2IngredientCommand(new Uom2UomCommand()), new Notes2NotesCommand());
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new Recipe()));
    }

    @Test
    public void convert() throws Exception {
        //given
        Notes notes = Notes.builder().id(NOTES_ID).build();

        Category category1 = Category.builder().id(CAT_ID_1).build();
        Category category2 = Category.builder().id(CAT_ID_2).build();

        Set<Category> categories = new HashSet<>();
        categories.add(category1);
        categories.add(category2);

        Ingredient ingredient1 = Ingredient.builder().id(INGRED_ID_1).build();
        Ingredient ingredient2 = Ingredient.builder().id(INGRED_ID_2).build();

        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);

        Recipe recipe = Recipe.builder()
                .id(RECIPE_ID)
                .cookTime(COOK_TIME)
                .prepTime(PREP_TIME)
                .description(DESCRIPTION)
                .difficulty(DIFFICULTY)
                .directions(DIRECTIONS)
                .servings(SERVINGS)
                .source(SOURCE)
                .url(URL)
                .notes(notes)
                .categories(categories)
                .ingredients(ingredients)
                .build();

        //when
        RecipeCommand command = converter.convert(recipe);

        //then
        assertNotNull(command);

        assertEquals(RECIPE_ID, command.getId());
        assertEquals(COOK_TIME, command.getCookTime());
        assertEquals(PREP_TIME, command.getPrepTime());
        assertEquals(DESCRIPTION, command.getDescription());
        assertEquals(DIFFICULTY, command.getDifficulty());
        assertEquals(DIRECTIONS, command.getDirections());
        assertEquals(SERVINGS, command.getServings());
        assertEquals(SOURCE, command.getSource());
        assertEquals(URL, command.getUrl());
        assertEquals(NOTES_ID, command.getNotes().getId());
        assertEquals(2, command.getCategories().size());
        assertEquals(2, command.getIngredients().size());
    }

}