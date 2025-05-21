package com.recipevault.service;

import com.recipevault.dto.RecipeCreateDTO;
import com.recipevault.dto.RecipeResponseDTO;
import com.recipevault.dto.RecipeUpdateDTO;
import org.springframework.data.domain.Page;

public interface RecipeService {
    RecipeResponseDTO createRecipe(RecipeCreateDTO dto);
    RecipeResponseDTO getRecipeById(Long id);
    Page<RecipeResponseDTO> getPagedRecipes(int page, int size, String title, String difficulty, Integer minIngredients, Integer maxIngredients);
    RecipeResponseDTO updateRecipe(Long id, RecipeUpdateDTO dto);
    void deleteRecipe(Long id);

}
