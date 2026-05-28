package com.tisu.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class MealVO {

    private String type;

    private List<DishSimpleVO> dishes;
}
