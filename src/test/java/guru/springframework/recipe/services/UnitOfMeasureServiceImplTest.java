package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.UomCommand;
import guru.springframework.recipe.converters.Uom2UomCommand;
import guru.springframework.recipe.domain.UnitOfMeasure;
import guru.springframework.recipe.repositories.UomRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UnitOfMeasureServiceImplTest {

    Uom2UomCommand command = new Uom2UomCommand();
    UomService service;

    @Mock
    UomRepository repository;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        service = new UnitOfMeasureServiceImpl(repository, command);
    }

    @Test
    public void listAllUoms() throws Exception {

        // given
        UnitOfMeasure uom1 = UnitOfMeasure.builder().id(1L).build();
        UnitOfMeasure uom2 = UnitOfMeasure.builder().id(2L).build();

        Set<UnitOfMeasure> uoms = new HashSet<>();
        Collections.addAll(uoms, uom1, uom2);

        when(repository.findAll()).thenReturn(uoms);

        // when
        Set<UomCommand> commands = service.listAllUoms();

        // then
        assertEquals(2, commands.size());
        verify(repository, times(1)).findAll();
    }
}