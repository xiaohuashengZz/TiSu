package com.tisu.service;

import com.tisu.common.result.PageResult;
import com.tisu.model.dto.DishDTO;
import com.tisu.model.vo.DishVO;

public interface AdminDishService {

    PageResult<DishVO> listDishes(String category, String keyword, int page, int size);

    void createDish(DishDTO dto);

    void updateDish(Long id, DishDTO dto);

    void deleteDish(Long id);
}
