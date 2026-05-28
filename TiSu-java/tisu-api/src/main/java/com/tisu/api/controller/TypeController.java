package com.tisu.api.controller;

import com.tisu.common.result.Result;
import com.tisu.model.entity.TypeEntity;
import com.tisu.service.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/types")
@RequiredArgsConstructor
public class TypeController {

    private final TypeService typeService;

    @GetMapping("/{group}")
    public Result<List<TypeEntity>> listByGroup(@PathVariable String group) {
        return Result.success(typeService.listByGroup(group));
    }
}
