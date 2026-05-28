package com.tisu.api.controller;

import com.tisu.common.result.PageResult;
import com.tisu.common.result.Result;
import com.tisu.model.dto.ExerciseDTO;
import com.tisu.model.vo.ExerciseVO;
import com.tisu.service.ExerciseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping
    public Result<PageResult<ExerciseVO>> list(
            @RequestParam(required = false) String muscleGroup,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(exerciseService.listExercises(muscleGroup, keyword, page, size));
    }

    @PostMapping
    public Result<Void> create(@RequestBody ExerciseDTO dto) {
        exerciseService.createExercise(dto);
        return Result.success();
    }
}
