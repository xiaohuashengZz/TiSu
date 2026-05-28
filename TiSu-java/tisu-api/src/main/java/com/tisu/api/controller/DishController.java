package com.tisu.api.controller;

import com.tisu.common.result.PageResult;
import com.tisu.common.result.Result;
import com.tisu.model.dto.DishDTO;
import com.tisu.model.dto.IngredientDTO;
import com.tisu.model.vo.DishVO;
import com.tisu.model.vo.IngredientVO;
import com.tisu.service.DishService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @GetMapping
    public Result<PageResult<DishVO>> list(
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(dishService.listDishes(source, category, keyword, page, size));
    }

    @PostMapping
    public Result<Void> create(HttpServletRequest request, @RequestBody DishDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        dishService.createDish(userId, dto);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<DishVO> detail(@PathVariable Long id) {
        return Result.success(dishService.getDishDetail(id));
    }

    @PutMapping("/{id}")
    public Result<Void> update(HttpServletRequest request, @PathVariable Long id, @RequestBody DishDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        dishService.updateDish(userId, id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        dishService.deleteDish(userId, id);
        return Result.success();
    }

    @GetMapping("/{id}/ingredients")
    public Result<List<IngredientVO>> getIngredients(@PathVariable Long id) {
        return Result.success(dishService.getIngredients(id));
    }

    @PutMapping("/{id}/ingredients")
    public Result<Void> updateIngredients(HttpServletRequest request, @PathVariable Long id,
                                          @RequestBody List<IngredientDTO> ingredients) {
        Long userId = (Long) request.getAttribute("userId");
        dishService.updateIngredients(userId, id, ingredients);
        return Result.success();
    }
}
