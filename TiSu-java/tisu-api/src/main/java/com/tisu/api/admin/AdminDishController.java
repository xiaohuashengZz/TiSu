package com.tisu.api.admin;

import com.tisu.common.result.PageResult;
import com.tisu.common.result.Result;
import com.tisu.model.dto.DishDTO;
import com.tisu.model.vo.DishVO;
import com.tisu.service.AdminDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dishes")
@RequiredArgsConstructor
public class AdminDishController {

    private final AdminDishService adminDishService;

    @GetMapping
    public Result<PageResult<DishVO>> list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(adminDishService.listDishes(category, keyword, page, size));
    }

    @PostMapping
    public Result<Void> create(@RequestBody DishDTO dto) {
        adminDishService.createDish(dto);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody DishDTO dto) {
        adminDishService.updateDish(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        adminDishService.deleteDish(id);
        return Result.success();
    }
}
