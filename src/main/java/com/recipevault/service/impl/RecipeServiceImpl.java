package com.recipevault.service.impl;

import com.recipevault.model.Recipe;
import com.recipevault.model.Ingredient;
import com.recipevault.model.Difficulty;
import com.recipevault.dto.RecipeCreateDTO;
import com.recipevault.dto.RecipeResponseDTO;
import com.recipevault.repository.RecipeRepository;
import com.recipevault.service.RecipeService;
import com.recipevault.dto.RecipeUpdateDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public RecipeResponseDTO createRecipe(RecipeCreateDTO dto) {
        // Build Recipe entity from DTO
        Recipe recipe = new Recipe(
                dto.getTitle(),
                dto.getInstructions(),
                Difficulty.valueOf(dto.getDifficulty()),
                dto.getCreatorName()
        );

        List<Ingredient> ingredients = dto.getIngredients().stream()
                .map(name -> new Ingredient(name, recipe))
                .collect(Collectors.toList());

        //recipe.setIngredients(ingredients);  comment out to allow hibernate to track and delete old ingredients
        recipe.getIngredients().clear(); // remove old ones
        //recipe.getIngredients().addAll(ingredients); // add new ones
        for (String name : dto.getIngredients()) {
            Ingredient ing = new Ingredient(name, recipe); // back-reference set here
            recipe.getIngredients().add(ing);
        }


        Recipe saved = recipeRepository.save(recipe);

        // Convert saved entity to response DTO
        RecipeResponseDTO response = new RecipeResponseDTO();
        response.setId(saved.getId());
        response.setTitle(saved.getTitle());
        response.setInstructions(saved.getInstructions());
        response.setDifficulty(saved.getDifficulty().name());
        response.setCreatorName(saved.getCreatorName());
        response.setCreatedDate(saved.getCreatedDate());
        response.setIngredients(
                saved.getIngredients().stream().map(Ingredient::getName).toList()
        );

        return response;
    }

    @Override
    public RecipeResponseDTO getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .map(recipe -> {
                    // Force load ingredients
                    recipe.getIngredients().size();

                    RecipeResponseDTO dto = new RecipeResponseDTO();
                    dto.setId(recipe.getId());
                    dto.setTitle(recipe.getTitle());
                    dto.setInstructions(recipe.getInstructions());
                    dto.setDifficulty(recipe.getDifficulty().name());
                    dto.setCreatorName(recipe.getCreatorName());
                    dto.setCreatedDate(recipe.getCreatedDate());
                    dto.setIngredients(
                            recipe.getIngredients().stream()
                                    .map(Ingredient::getName)
                                    .toList()
                    );
                    return dto;
                })
                .orElse(null);
    }

    @Override
    public Page<RecipeResponseDTO> getPagedRecipes(int page, int size, String title, String difficulty, Integer minIngredients, Integer maxIngredients) {
        Pageable pageable = PageRequest.of(page, size);
        List<Recipe> all = recipeRepository.findAll();

        Stream<Recipe> stream = all.stream();

        if (title != null && !title.isEmpty()) {
            stream = stream.filter(r -> r.getTitle().toLowerCase().contains(title.toLowerCase()));
        }

        if (difficulty != null && !difficulty.isEmpty()) {
            try {
                Difficulty diffEnum = Difficulty.valueOf(difficulty.toUpperCase());
                stream = stream.filter(r -> r.getDifficulty() == diffEnum);
            } catch (IllegalArgumentException ignored) {}
        }

        if (minIngredients != null) {
            stream = stream.filter(r -> r.getIngredients().size() >= minIngredients);
        }

        if (maxIngredients != null) {
            stream = stream.filter(r -> r.getIngredients().size() <= maxIngredients);
        }

        List<Recipe> filtered = stream.collect(Collectors.toList());

        int start = Math.min(page * size, filtered.size());
        int end = Math.min(start + size, filtered.size());
        List<Recipe> pageContent = filtered.subList(start, end);

        List<RecipeResponseDTO> responseList = pageContent.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(responseList, pageable, filtered.size());
    }

    private RecipeResponseDTO convertToResponseDTO(Recipe recipe) {
        RecipeResponseDTO dto = new RecipeResponseDTO();
        dto.setId(recipe.getId());
        dto.setTitle(recipe.getTitle());
        dto.setDifficulty(recipe.getDifficulty().name());
        dto.setCreatorName(recipe.getCreatorName());
        dto.setInstructions(recipe.getInstructions());
        dto.setImageUrl(recipe.getImageUrl());
        dto.setCreatedDate(recipe.getCreatedDate());

        if (recipe.getIngredients() != null) {
            dto.setIngredients(
                    recipe.getIngredients().stream()
                            .map(Ingredient::getName)
                            .collect(Collectors.toList())
            );
        } else {
            dto.setIngredients(Collections.emptyList());
        }

        return dto;
    }

    @Override
    public RecipeResponseDTO updateRecipe(Long id, RecipeUpdateDTO dto) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        recipe.setTitle(dto.getTitle());
        recipe.setInstructions(dto.getInstructions());
        recipe.setDifficulty(Difficulty.valueOf(dto.getDifficulty()));
        recipe.setCreatorName(dto.getCreatorName());

        // rebuild ingredients
        List<Ingredient> ingredients = dto.getIngredients().stream()
                .map(name -> new Ingredient(name, recipe))
                .toList();

        recipe.getIngredients().clear();
        recipe.getIngredients().addAll(ingredients);

        Recipe saved = recipeRepository.save(recipe);

        RecipeResponseDTO response = new RecipeResponseDTO();
        response.setId(saved.getId());
        response.setTitle(saved.getTitle());
        response.setInstructions(saved.getInstructions());
        response.setDifficulty(saved.getDifficulty().name());
        response.setCreatorName(saved.getCreatorName());
        response.setCreatedDate(saved.getCreatedDate());
        response.setIngredients(saved.getIngredients().stream().map(Ingredient::getName).toList());

        return response;
    }

    @Override
    public void deleteRecipe(Long id) {
        if (!recipeRepository.existsById(id)) {
            throw new RuntimeException("Recipe not found");
        }
        recipeRepository.deleteById(id);
    }

}
