package guru.springframework.recipe.commands;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotesCommand {

    private Long id;
    private String recipeNotes;
}
