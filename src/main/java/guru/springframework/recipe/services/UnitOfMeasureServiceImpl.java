package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.UomCommand;
import guru.springframework.recipe.converters.Uom2UomCommand;
import guru.springframework.recipe.repositories.UomRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UnitOfMeasureServiceImpl implements UomService {

    private final UomRepository unitOfMeasureRepository;
    private final Uom2UomCommand converter;

    public UnitOfMeasureServiceImpl(UomRepository unitOfMeasureRepository, Uom2UomCommand converter) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.converter = converter;
    }

    @Override
    public Set<UomCommand> listAllUoms() {

        return StreamSupport.stream(unitOfMeasureRepository.findAll().spliterator(), false)
                .map(uom -> converter.convert(uom))
                .collect(Collectors.toSet());
    }
}
