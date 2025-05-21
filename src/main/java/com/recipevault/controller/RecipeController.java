/*Controller handles HTTP requests - CRUD, in HTTP format (header with content)
*
* */

package com.recipevault.controller;

import com.recipevault.model.Recipe;
import com.recipevault.model.Ingredient;
import com.recipevault.model.Difficulty;
import com.recipevault.repository.RecipeRepository;
import com.recipevault.service.RecipeService;
import com.recipevault.dto.RecipeResponseDTO;
import com.recipevault.dto.RecipeUpdateDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeRepository recipeRepository;

    // Inject the repository
    @Autowired
    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // GET /recipes
    @GetMapping("/paged")
    public ResponseEntity<Page<RecipeResponseDTO>> getPagedRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) Integer minIngredients,
            @RequestParam(required = false) Integer maxIngredients) {

        Page<RecipeResponseDTO> results = recipeService.getPagedRecipes(page, size, title, difficulty, minIngredients, maxIngredients);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }


    @GetMapping("/filter")
    public ResponseEntity<List<Recipe>> filterRecipes(
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) Integer ingredientCount) {

        Difficulty parsedDifficulty = null;

        try {
            if (difficulty != null && !difficulty.isEmpty()) {
                parsedDifficulty = Difficulty.valueOf(difficulty.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }

        List<Recipe> results = recipeRepository.findByDifficultyAndIngredientCount(parsedDifficulty, ingredientCount);
        results.forEach(r -> r.getIngredients().size()); // Load ingredients

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchByTitle(@RequestParam String title) {
        List<Recipe> results = recipeRepository.findByTitleContainingIgnoreCase(title);
        results.forEach(r -> r.getIngredients().size()); // load ingredients
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponseDTO> getRecipeById(@PathVariable Long id) {
        RecipeResponseDTO dto = recipeService.getRecipeById(id);
        return (dto != null) ?
                new ResponseEntity<>(dto, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("")
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();

        // Force ingredients to load (since FetchType.LAZY is default)
        for (Recipe recipe : recipes) {
            recipe.getIngredients().size();  // triggers loading
        }

        return recipes;
    }

    // POST /recipes
    @PostMapping("")
    public ResponseEntity<Recipe> createRecipe(@RequestBody Map<String, Object> body) {
        // 1. Parse basic recipe fields
        String title = (String) body.get("title");
        String instructions = (String) body.get("instructions");
        String difficultyStr = (String) body.get("difficulty");
        String creatorName = (String) body.get("creatorName");

        Difficulty difficulty = Difficulty.valueOf(difficultyStr);

        // 2. Create recipe object
        Recipe recipe = new Recipe(title, instructions, difficulty, creatorName);

        // 3. Parse ingredients (if any)
        List<String> ingredientNames = (List<String>) body.get("ingredients");
        if (ingredientNames != null) {
            List<Ingredient> ingredients = new ArrayList<>();
            for (String name : ingredientNames) {
                ingredients.add(new Ingredient(name, recipe));
            }
            recipe.setIngredients(ingredients);
        }

        // 4. Save and return
        Recipe saved = recipeRepository.save(recipe);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // PUT /recipes
    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponseDTO> updateRecipe(
            @PathVariable Long id,
            @RequestBody RecipeUpdateDTO dto) {
        return ResponseEntity.ok(recipeService.updateRecipe(id, dto));
    }

    //DELETE /recipes
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long id) {
        try {
            recipeService.deleteRecipe(id);
            return ResponseEntity.ok("Recipe deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}

