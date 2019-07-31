package guru.springframework.recipe.commands;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UomCommand {

    private Long id;
    private String description;
}
