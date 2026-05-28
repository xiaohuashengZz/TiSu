package com.tisu.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class DishDTO {

    private String name;

    private String description;

    private String category;

    private List<IngredientDTO> ingredients;
}
