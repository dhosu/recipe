package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.domain.Ingredient;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientCommand2Ingredient implements Converter<IngredientCommand, Ingredient> {

    private final UomCommand2Uom uomConverter;

    public IngredientCommand2Ingredient(UomCommand2Uom uomConverter) {
        this.uomConverter = uomConverter;
    }

    public IngredientCommand2Ingredient() {
        uomConverter = new UomCommand2Uom();
    }

    @Synchronized
    @Nullable
    @Override
    public Ingredient convert(IngredientCommand source) {

        if (source == null)
            return null;

        return Ingredient.builder()
                .id(source.getId())
                .description(source.getDescription())
                .amount(source.getAmount())
                .uom(uomConverter.convert(source.getUom()))
                .build();
    }
}
