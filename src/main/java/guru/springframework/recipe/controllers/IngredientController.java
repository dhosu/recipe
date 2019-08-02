package guru.springframework.recipe.controllers;

import guru.springframework.recipe.commands.IngredientCommand;
import guru.springframework.recipe.commands.RecipeCommand;
import guru.springframework.recipe.commands.UomCommand;
import guru.springframework.recipe.services.IngredientService;
import guru.springframework.recipe.services.RecipeService;
import guru.springframework.recipe.services.UomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IngredientController {

    private RecipeService recipeService;
    private IngredientService ingredientService;
    private UomService uomService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UomService uomService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.uomService = uomService;
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model) {

        // use command object to avoid lazy load errors in Thymeleaf
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));
        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/show")
    public String showRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {

        Long _recipeId = Long.valueOf(recipeId);
        Long _id = Long.valueOf(id);

        model.addAttribute("ingredient", ingredientService.findByIds(_recipeId, _id));

        return "recipe/ingredient/show";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {

        Long _recipeId = Long.valueOf(recipeId);
        Long _id = Long.valueOf(id);

        model.addAttribute("ingredient", ingredientService.findByIds(_recipeId, _id));
        model.addAttribute("uomList", uomService.listAllUoms());

        return "recipe/ingredient/ingredientform";
    }


    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command) {

        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        log.debug("saved recipe id:" + savedCommand.getRecipeId());
        log.debug("saved ingredient id:" + savedCommand.getId());

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable String recipeId, Model model) {

        Long _recipeId = Long.valueOf(recipeId);

        // make sure you have a good id value
        RecipeCommand recipeCommand = recipeService.findCommandById(_recipeId);
        // todo raise exception if null

        // need to return back parent id for hidden form property
        IngredientCommand ingredientCommand = IngredientCommand.builder()
                .recipeId(_recipeId)
                .uom(new UomCommand())
                .build();

        model.addAttribute("ingredient", ingredientCommand);
        model.addAttribute("uomList", uomService.listAllUoms());

        return "recipe/ingredient/ingredientform";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {

        Long _recipeId = Long.valueOf(recipeId);
        Long _id = Long.valueOf(id);

        ingredientService.deleteByIds(_recipeId, _id);

        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}
