package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.IngredientCommand;

public interface IngredientService {

    IngredientCommand findByIds(Long recipeId, long ingredientId);

    IngredientCommand saveIngredientCommand(IngredientCommand command);
}
