package com.tisu.service;

import com.tisu.common.result.PageResult;
import com.tisu.model.dto.FoodDTO;
import com.tisu.model.vo.FoodVO;

public interface FoodService {

    PageResult<FoodVO> listFoods(Long userId, String source, String category, String keyword, int page, int size);

    void createFood(Long userId, FoodDTO dto);

    void updateFood(Long userId, Long foodId, FoodDTO dto);

    void deleteFood(Long userId, Long foodId);
}
