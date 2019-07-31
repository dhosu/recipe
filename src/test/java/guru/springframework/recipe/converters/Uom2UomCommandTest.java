package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.UomCommand;
import guru.springframework.recipe.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Uom2UomCommandTest {

    public static final String DESCRIPTION = "description";
    public static final Long LONG_VALUE = 1L;

    UomCommand2Uom converter;

    @Before
    public void setUp() throws Exception {
        converter = new UomCommand2Uom();
    }

    @Test
    public void testNullParamter() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new UomCommand()));
    }

    @Test
    public void convert() throws Exception {
        //given
        UomCommand command = UomCommand.builder().id(LONG_VALUE).description(DESCRIPTION).build();

        //when
        UnitOfMeasure uom = converter.convert(command);

        //then
        assertNotNull(uom);

        assertEquals(LONG_VALUE, uom.getId());
        assertEquals(DESCRIPTION, uom.getDescription());
    }

}