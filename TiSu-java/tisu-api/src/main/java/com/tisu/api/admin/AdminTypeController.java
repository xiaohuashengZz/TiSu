package com.tisu.api.admin;

import com.tisu.common.result.Result;
import com.tisu.model.dto.TypeDTO;
import com.tisu.model.entity.TypeEntity;
import com.tisu.service.AdminTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/types")
@RequiredArgsConstructor
public class AdminTypeController {

    private final AdminTypeService adminTypeService;

    @GetMapping
    public Result<List<TypeEntity>> list(@RequestParam(required = false) String typeGroup) {
        return Result.success(adminTypeService.listTypes(typeGroup));
    }

    @PostMapping
    public Result<Void> create(@RequestBody TypeDTO dto) {
        adminTypeService.createType(dto);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody TypeDTO dto) {
        adminTypeService.updateType(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        adminTypeService.deleteType(id);
        return Result.success();
    }
}
