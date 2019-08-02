package guru.springframework.recipe.services;

import guru.springframework.recipe.commands.UomCommand;

import java.util.Set;

public interface UomService {

    Set<UomCommand> listAllUoms();
}
