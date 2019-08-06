package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.converters.Recipe2RecipeCommand;
import guru.springframework.recipe.converters.RecipeCommand2Recipe;
import guru.springframework.recipe.domain.Recipe;
import guru.springframework.recipe.exceptions.NotFoundException;
import guru.springframework.recipe.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommand2Recipe recipeCommand2Recipe;
    private final Recipe2RecipeCommand recipe2RecipeCommand;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommand2Recipe recipeCommand2Recipe, Recipe2RecipeCommand recipe2RecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommand2Recipe = recipeCommand2Recipe;
        this.recipe2RecipeCommand = recipe2RecipeCommand;
    }

    @Override
    public Set<Recipe> getRecipes() {

        Set<Recipe> recipes = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);

        return recipes;
    }

    public Recipe findById(Long id) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(id);

        if (!recipeOptional.isPresent()) {
            throw new NotFoundException("Recipe Not Found! ID value: " + id);
        }

        return recipeOptional.get();
    }

    @Transactional
    @Override
    public RecipeCommand findCommandById(Long id) {
        return recipe2RecipeCommand.convert(findById(id));
    }

    @Override
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }

    @Transactional
    @Override
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {

        Recipe detachedRecipe = recipeCommand2Recipe.convert(recipeCommand);
        Recipe savedRecipe = recipeRepository.save(detachedRecipe);

        log.debug("Saved recipe id:" + savedRecipe.getId());

        return recipe2RecipeCommand.convert(savedRecipe);
    }
}