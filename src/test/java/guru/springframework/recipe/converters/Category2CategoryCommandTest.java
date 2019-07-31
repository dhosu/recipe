package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.CategoryCommand;
import guru.springframework.recipe.domain.Category;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Category2CategoryCommandTest {

    public static final Long ID_VALUE = Long.valueOf(1L);
    public static final String DESCRIPTION = "description";

    CategoryCommand2Category converter;

    @Before
    public void setUp() throws Exception {
        converter = new CategoryCommand2Category();
    }

    @Test
    public void testNullObject() throws Exception {
        assertNotNull(converter.convert(new CategoryCommand()));
    }

    @Test
    public void convert() throws Exception {

        // given
        CategoryCommand categoryCommand = CategoryCommand.builder().id(ID_VALUE).description(DESCRIPTION).build();

        // when
        Category category = converter.convert(categoryCommand);

        // then
        assertEquals(ID_VALUE, category.getId());
        assertEquals(DESCRIPTION, category.getDescription());
    }

}