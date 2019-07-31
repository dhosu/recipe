package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.UomCommand;
import guru.springframework.recipe.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UomCommand2UomTest {

    public static final String DESCRIPTION = "description";
    public static final Long LONG_VALUE = new Long(1L);

    Uom2UomCommand converter;

    @Before
    public void setUp() throws Exception {
        converter = new Uom2UomCommand();
    }

    @Test
    public void testNullObjectConvert() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObj() throws Exception {
        assertNotNull(converter.convert(new UnitOfMeasure()));
    }

    @Test
    public void convert() throws Exception {

        //given
        UnitOfMeasure uom = UnitOfMeasure.builder().id(LONG_VALUE).description(DESCRIPTION).build();

        //when
        UomCommand uomc = converter.convert(uom);

        //then
        assertEquals(LONG_VALUE, uomc.getId());
        assertEquals(DESCRIPTION, uomc.getDescription());
    }
}