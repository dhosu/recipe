package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.UomCommand;
import guru.springframework.recipe.domain.UnitOfMeasure;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UomCommand2Uom implements Converter<UomCommand, UnitOfMeasure> {

    @Synchronized
    @Nullable
    @Override
    public UnitOfMeasure convert(UomCommand source) {

        if (source == null)
            return null;

        return UnitOfMeasure.builder().id(source.getId()).description(source.getDescription()).build();
    }
}
