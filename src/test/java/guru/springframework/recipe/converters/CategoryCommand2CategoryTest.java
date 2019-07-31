package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.CategoryCommand;
import guru.springframework.recipe.domain.Category;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class CategoryCommand2CategoryTest {

    public static final Long ID_VALUE = new Long(1L);
    public static final String DESCRIPTION = "descript";

    Category2CategoryCommand convter;

    @Before
    public void setUp() throws Exception {
        convter = new Category2CategoryCommand();
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(convter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(convter.convert(new Category()));
    }

    @Test
    public void convert() throws Exception {

        //given
        Category category = Category.builder().id(ID_VALUE).description(DESCRIPTION).build();

        //when
        CategoryCommand categoryCommand = convter.convert(category);

        //then
        assertEquals(ID_VALUE, categoryCommand.getId());
        assertEquals(DESCRIPTION, categoryCommand.getDescription());

    }

}