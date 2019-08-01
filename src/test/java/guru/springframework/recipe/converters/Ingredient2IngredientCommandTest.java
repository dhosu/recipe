package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.commands.UomCommand;
import guru.springframework.recipe.domain.Ingredient;
import guru.springframework.recipe.domain.Recipe;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class Ingredient2IngredientCommandTest {

    public static final Recipe RECIPE = new Recipe();
    public static final BigDecimal AMOUNT = new BigDecimal("1");
    public static final String DESCRIPTION = "Cheeseburger";
    public static final Long ID_VALUE = new Long(1L);
    public static final Long UOM_ID = new Long(2L);

    IngredientCommand2Ingredient converter;

    @Before
    public void setUp() throws Exception {
        converter = new IngredientCommand2Ingredient(new UomCommand2Uom());
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new IngredientCommand()));
    }

    @Test
    public void convert() throws Exception {

        //given
        UomCommand unitOfMeasureCommand = UomCommand.builder().id(UOM_ID).build();

        IngredientCommand command = IngredientCommand.builder()
                .id(ID_VALUE)
                .amount(AMOUNT)
                .description(DESCRIPTION)
                .uom(unitOfMeasureCommand)
                .build();

        //when
        Ingredient ingredient = converter.convert(command);

        //then
        assertNotNull(ingredient);
        assertNotNull(ingredient.getUom());

        assertEquals(ID_VALUE, ingredient.getId());
        assertEquals(AMOUNT, ingredient.getAmount());
        assertEquals(DESCRIPTION, ingredient.getDescription());
        assertEquals(UOM_ID, ingredient.getUom().getId());
    }

    @Test
    public void convertWithNullUOM() throws Exception {

        //given
        IngredientCommand command = IngredientCommand.builder().id(ID_VALUE).amount(AMOUNT).description(DESCRIPTION).build();

        //when
        Ingredient ingredient = converter.convert(command);

        //then
        assertNotNull(ingredient);
        assertNull(ingredient.getUom());

        assertEquals(ID_VALUE, ingredient.getId());
        assertEquals(AMOUNT, ingredient.getAmount());
        assertEquals(DESCRIPTION, ingredient.getDescription());
    }
}