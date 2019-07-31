package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.UomCommand;
import guru.springframework.recipe.domain.UnitOfMeasure;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class Uom2UomCommand implements Converter<UnitOfMeasure, UomCommand> {

    @Synchronized
    @Nullable
    @Override
    public UomCommand convert(UnitOfMeasure source) {

        if (source == null)
            return null;

        return UomCommand.builder()
                .id(source.getId())
                .description(source.getDescription())
                .build();
    }
}
