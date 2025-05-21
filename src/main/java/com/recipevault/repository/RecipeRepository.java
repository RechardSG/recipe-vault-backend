package com.recipevault.repository;
import com.recipevault.model.Recipe;
import com.recipevault.model.Difficulty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Page<Recipe> findAll(Pageable pageable);  // for pagination

    @Query("SELECT r FROM Recipe r WHERE " +
            "(:difficulty IS NULL OR r.difficulty = :difficulty) AND " +
            "(:ingredientCount IS NULL OR SIZE(r.ingredients) = :ingredientCount)")
    List<Recipe> findByDifficultyAndIngredientCount(
            @Param("difficulty") Difficulty difficulty,
            @Param("ingredientCount") Integer ingredientCount);

    List<Recipe> findByTitleContainingIgnoreCase(String title);

}


