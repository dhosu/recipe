package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.*;
import guru.springframework.recipe.domain.Difficulty;
import guru.springframework.recipe.domain.Recipe;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class RecipeCommand2RecipeTest {

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
    public static final Byte[] IMAGE = new Byte[] {1,2,3,4,5};

    RecipeCommand2Recipe converter;

    @Before
    public void setUp() throws Exception {
        converter = new RecipeCommand2Recipe(new CategoryCommand2Category(), new IngredientCommand2Ingredient(new UomCommand2Uom()), new NotesCommand2Notes());
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new RecipeCommand()));
    }

    @Test
    public void convert() throws Exception {

        //given
        NotesCommand notes = NotesCommand.builder().id(NOTES_ID).build();

        CategoryCommand category1 = CategoryCommand.builder().id(CAT_ID_1).build();
        CategoryCommand category2 = CategoryCommand.builder().id(CAT_ID_2).build();

        Set<CategoryCommand> categories = new HashSet<>();
        categories.add(category1);
        categories.add(category2);

        IngredientCommand ingredient1 = IngredientCommand.builder().id(INGRED_ID_1).build();
        IngredientCommand ingredient2 = IngredientCommand.builder().id(INGRED_ID_2).build();

        Set<IngredientCommand> ingredients = new HashSet<>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);

        RecipeCommand recipeCommand = RecipeCommand.builder()
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
                .image(IMAGE)
                .categories(categories)
                .ingredients(ingredients)
                .build();


        //when
        Recipe recipe  = converter.convert(recipeCommand);

        assertNotNull(recipe);

        assertEquals(RECIPE_ID, recipe.getId());
        assertEquals(COOK_TIME, recipe.getCookTime());
        assertEquals(PREP_TIME, recipe.getPrepTime());
        assertEquals(DESCRIPTION, recipe.getDescription());
        assertEquals(DIFFICULTY, recipe.getDifficulty());
        assertEquals(DIRECTIONS, recipe.getDirections());
        assertEquals(SERVINGS, recipe.getServings());
        assertEquals(SOURCE, recipe.getSource());
        assertEquals(URL, recipe.getUrl());
        assertEquals(NOTES_ID, recipe.getNotes().getId());
        assertEquals(2, recipe.getCategories().size());
        assertEquals(2, recipe.getIngredients().size());
        assertArrayEquals(IMAGE, recipe.getImage());
    }

}