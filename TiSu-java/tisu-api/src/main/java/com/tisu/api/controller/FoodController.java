package com.tisu.api.controller;

import com.tisu.common.result.PageResult;
import com.tisu.common.result.Result;
import com.tisu.model.dto.FoodDTO;
import com.tisu.model.vo.FoodVO;
import com.tisu.service.FoodService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @GetMapping
    public Result<PageResult<FoodVO>> list(
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(foodService.listFoods(source, category, keyword, page, size));
    }

    @PostMapping
    public Result<Void> create(HttpServletRequest request, @RequestBody FoodDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        foodService.createFood(userId, dto);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(HttpServletRequest request, @PathVariable Long id, @RequestBody FoodDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        foodService.updateFood(userId, id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        foodService.deleteFood(userId, id);
        return Result.success();
    }
}
