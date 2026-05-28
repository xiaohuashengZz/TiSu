package com.tisu.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class DishSimpleVO {

    private Long id;

    private String name;

    private Integer calories;

    private List<IngredientVO> ingredients;
}
