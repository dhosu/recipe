package guru.springframework.recipe.commands;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientCommand {

    private Long id;
    private String description;
    private BigDecimal amount;
    private UomCommand uom;
}
