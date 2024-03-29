package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.domain.Ingredient;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class Ingredient2IngredientCommand implements Converter<Ingredient, IngredientCommand> {

    private final Uom2UomCommand uomConverter;

    public Ingredient2IngredientCommand(Uom2UomCommand uomConverter) {
        this.uomConverter = uomConverter;
    }

    public Ingredient2IngredientCommand() {
        uomConverter = new Uom2UomCommand();
    }

    @Synchronized
    @Nullable
    @Override
    public IngredientCommand convert(Ingredient source) {

        if (source == null)
            return null;

        boolean isRecipeSet = source.getRecipe() != null;

        return IngredientCommand.builder()
                .id(source.getId())
                .recipeId(isRecipeSet ? source.getRecipe().getId() : 0)
                .description(source.getDescription())
                .amount(source.getAmount())
                .uom(uomConverter.convert(source.getUom()))
                .build();
    }
}
