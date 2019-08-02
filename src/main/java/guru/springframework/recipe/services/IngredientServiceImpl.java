package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.converters.Ingredient2IngredientCommand;
import guru.springframework.recipe.converters.IngredientCommand2Ingredient;
import guru.springframework.recipe.domain.Ingredient;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.repositories.RecipeRepository;
import guru.springframework.recipe.repositories.UomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private RecipeRepository recipeRepository;
    private UomRepository uomRepository;
    private IngredientCommand2Ingredient ingredientConverter;
    private Ingredient2IngredientCommand ingredientCommandConverter;

    public IngredientServiceImpl(RecipeRepository recipeRepository, UomRepository uomRepository, IngredientCommand2Ingredient ingredientConverter, Ingredient2IngredientCommand ingredientCommandConverter) {
        this.recipeRepository = recipeRepository;
        this.uomRepository = uomRepository;
        this.ingredientConverter = ingredientConverter;
        this.ingredientCommandConverter = ingredientCommandConverter;
    }

    @Override
    public IngredientCommand findByIds(Long recipeId, long ingredientId) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (!recipeOptional.isPresent()) {
            // todo impl error handling
            log.error("recipe id not found. Id: " + recipeId);
        }

        Recipe recipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommandOptional =
                recipe.getIngredients().stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .map(ingredient -> ingredientCommandConverter.convert(ingredient))
                    .findFirst();

        if (!ingredientCommandOptional.isPresent()) {
            // todo impl error handling
            log.error("ingredient id not found. Id: " + ingredientId);
        }

        return ingredientCommandOptional.get();
    }

    @Transactional
    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if (!recipeOptional.isPresent()) {

            // todo toss error if not found!
            log.error("Recipe not found for id: " + command.getRecipeId());
            return new IngredientCommand();
        }

        Recipe recipe = recipeOptional.get();

        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst();

        if (ingredientOptional.isPresent()) {
            Ingredient ingredientFound = ingredientOptional.get();
            ingredientFound.setDescription(command.getDescription());
            ingredientFound.setAmount(command.getAmount());
            // todo address this
            ingredientFound.setUom(uomRepository.findById(command.getUom().getId()).orElseThrow(() -> new RuntimeException("UOM_NOT_FOUND")));
        } else {
            Ingredient ingredient = ingredientConverter.convert(command);
            ingredient.setRecipe(recipe);
            recipe.addIngredient(ingredient);
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

        // to do check for fail

        Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findFirst();

        if (!savedIngredientOptional.isPresent()) {
            // not totally safe but best guess
            savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(i -> i.getDescription().equals(command.getDescription()) &&
                                i.getAmount().equals(command.getAmount()) &&
                                i.getUom().getId().equals(command.getUom().getId()))
                    .findFirst();
        }

        return ingredientCommandConverter.convert(savedIngredientOptional.get());
    }

    @Override
    public void deleteByIds(Long recipeId, Long id) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (!recipeOptional.isPresent()) {

            // todo toss error if not found!
            log.error("Recipe not found for id: " + recipeId);
            return;
        }

        Recipe recipe = recipeOptional.get();
        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                .filter(i -> i.getId() == id)
                .findFirst();

        if (ingredientOptional.isPresent()) {
            ingredientOptional.get().setRecipe(null);
            recipe.getIngredients().remove(ingredientOptional.get());

            recipeRepository.save(recipe);
        } else {
            log.debug("Recipe id not found. Id: " + recipeId);
        }

    }
}
