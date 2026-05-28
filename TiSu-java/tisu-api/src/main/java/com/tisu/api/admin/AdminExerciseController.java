package com.tisu.api.admin;

import com.tisu.common.result.PageResult;
import com.tisu.common.result.Result;
import com.tisu.model.dto.ExerciseDTO;
import com.tisu.model.vo.ExerciseVO;
import com.tisu.service.AdminExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/exercises")
@RequiredArgsConstructor
public class AdminExerciseController {

    private final AdminExerciseService adminExerciseService;

    @GetMapping
    public Result<PageResult<ExerciseVO>> list(
            @RequestParam(required = false) String muscleGroup,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(adminExerciseService.listExercises(muscleGroup, keyword, page, size));
    }

    @PostMapping
    public Result<Void> create(@RequestBody ExerciseDTO dto) {
        adminExerciseService.createExercise(dto);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody ExerciseDTO dto) {
        adminExerciseService.updateExercise(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        adminExerciseService.deleteExercise(id);
        return Result.success();
    }
}
