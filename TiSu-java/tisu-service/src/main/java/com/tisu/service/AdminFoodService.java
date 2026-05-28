package com.tisu.service;

import com.tisu.common.result.PageResult;
import com.tisu.model.dto.FoodDTO;
import com.tisu.model.vo.FoodVO;

public interface AdminFoodService {

    PageResult<FoodVO> listFoods(String source, String category, String keyword, int page, int size);

    void createFood(FoodDTO dto);

    void updateFood(Long id, FoodDTO dto);

    void deleteFood(Long id);
}
