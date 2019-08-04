package guru.springframework.recipe.converters;

import guru.springframework.recipe.commands.CategoryCommand;
import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.domain.Category;
import guru.springframework.recipe.domain.Ingredient;
import guru.springframework.recipe.domain.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Recipe2RecipeCommand implements Converter<Recipe, RecipeCommand> {

    private final Category2CategoryCommand categoryConverter;
    private final Ingredient2IngredientCommand ingredientConverter;
    private final Notes2NotesCommand notesConverter;

    public Recipe2RecipeCommand(Category2CategoryCommand categoryConverter, Ingredient2IngredientCommand ingredientConverter, Notes2NotesCommand notesConverter) {
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public RecipeCommand convert(Recipe source) {

        if (source == null)
            return null;

        return RecipeCommand.builder()
                .id(source.getId())
                .cookTime(source.getCookTime())
                .prepTime(source.getPrepTime())
                .description(source.getDescription())
                .difficulty(source.getDifficulty())
                .directions(source.getDirections())
                .servings(source.getServings())
                .source(source.getSource())
                .url(source.getUrl())
                .image(source.getImage())
                .notes(notesConverter.convert(source.getNotes()))
                .categories(getCategoriesConverted(source.getCategories()))
                .ingredients(getIngredientsConverter(source.getIngredients()))
                .build();
    }

    private Set<IngredientCommand> getIngredientsConverter(Set<Ingredient> ingredients) {

        return ingredients.stream()
                .map(ingredient -> ingredientConverter.convert(ingredient))
                .collect(Collectors.toSet());
    }

    private Set<CategoryCommand> getCategoriesConverted(Set<Category> categories) {

        return categories.stream()
                .map(category -> categoryConverter.convert(category))
                .collect(Collectors.toSet());
    }
}
