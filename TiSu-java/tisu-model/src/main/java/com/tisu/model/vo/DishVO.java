package com.tisu.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class DishVO {

    private Long id;

    private String name;

    private String description;

    private Integer calories;

    private String category;

    private String source;

    private List<IngredientVO> ingredients;
}
