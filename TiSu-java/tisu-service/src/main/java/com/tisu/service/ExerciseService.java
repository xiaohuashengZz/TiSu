package com.tisu.service;

import com.tisu.common.result.PageResult;
import com.tisu.model.dto.ExerciseDTO;
import com.tisu.model.vo.ExerciseVO;

public interface ExerciseService {

    PageResult<ExerciseVO> listExercises(String muscleGroup, String keyword, int page, int size);

    void createExercise(ExerciseDTO dto);

    void updateExercise(Long id, ExerciseDTO dto);

    void deleteExercise(Long id);
}
