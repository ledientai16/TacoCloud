package org.idk.tacocloud.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class Taco {
    @NotNull
    @Size(min = 5, message = "Name must more than 5 characters!")
    private String name;
    @NotNull
    @Size(min = 1, message = "Choose at least 1 ingredient of your taco!")
    private List<Ingredient> ingredients;

}
