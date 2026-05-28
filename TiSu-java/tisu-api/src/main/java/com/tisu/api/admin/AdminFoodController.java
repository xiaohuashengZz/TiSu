package com.tisu.api.admin;

import com.tisu.common.result.PageResult;
import com.tisu.common.result.Result;
import com.tisu.model.dto.FoodDTO;
import com.tisu.model.vo.FoodVO;
import com.tisu.service.AdminFoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/foods")
@RequiredArgsConstructor
public class AdminFoodController {

    private final AdminFoodService adminFoodService;

    @GetMapping
    public Result<PageResult<FoodVO>> list(
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(adminFoodService.listFoods(source, category, keyword, page, size));
    }

    @PostMapping
    public Result<Void> create(@RequestBody FoodDTO dto) {
        adminFoodService.createFood(dto);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody FoodDTO dto) {
        adminFoodService.updateFood(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        adminFoodService.deleteFood(id);
        return Result.success();
    }
}
