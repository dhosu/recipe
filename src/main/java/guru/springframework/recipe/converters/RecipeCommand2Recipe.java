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
public class RecipeCommand2Recipe implements Converter<RecipeCommand, Recipe> {

    private final CategoryCommand2Category categoryConverter;
    private final IngredientCommand2Ingredient ingredientConverter;
    private final NotesCommand2Notes notesConverter;

    public RecipeCommand2Recipe(CategoryCommand2Category categoryConverter, IngredientCommand2Ingredient ingredientConverter, NotesCommand2Notes notesConverter) {
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public Recipe convert(RecipeCommand source) {

        if (source == null)
            return null;

        return Recipe.builder()
                .id(source.getId())
                .cookTime(source.getCookTime())
                .prepTime(source.getPrepTime())
                .description(source.getDescription())
                .difficulty(source.getDifficulty())
                .directions(source.getDirections())
                .servings(source.getServings())
                .source(source.getSource())
                .url(source.getUrl())
                .notes(notesConverter.convert(source.getNotes()))
                .categories(convertCategories(source.getCategories()))
                .ingredients(convertIngredients(source.getIngredients()))
                .build();
    }

    private Set<Ingredient> convertIngredients(Set<IngredientCommand> ingredients) {

        if (ingredients == null)
            return null;

        return ingredients.stream()
                .map(ingredientCommand -> ingredientConverter.convert(ingredientCommand))
                .collect(Collectors.toSet());
    }

    private Set<Category> convertCategories(Set<CategoryCommand> categories) {

        if (categories == null)
            return null;

        return categories.stream()
                .map(categoryCommand -> categoryConverter.convert(categoryCommand))
                .collect(Collectors.toSet());
    }
}
