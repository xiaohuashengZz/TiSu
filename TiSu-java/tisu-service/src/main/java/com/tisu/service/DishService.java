package com.tisu.service;

import com.tisu.common.result.PageResult;
import com.tisu.model.dto.DishDTO;
import com.tisu.model.dto.IngredientDTO;
import com.tisu.model.vo.DishVO;
import com.tisu.model.vo.IngredientVO;

import java.util.List;

public interface DishService {

    PageResult<DishVO> listDishes(Long userId, String source, String category, String keyword, int page, int size);

    DishVO getDishDetail(Long dishId);

    void createDish(Long userId, DishDTO dto);

    void updateDish(Long userId, Long dishId, DishDTO dto);

    void deleteDish(Long userId, Long dishId);

    List<IngredientVO> getIngredients(Long dishId);

    void updateIngredients(Long userId, Long dishId, List<IngredientDTO> ingredients);
}
