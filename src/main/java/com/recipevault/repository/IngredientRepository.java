package com.recipevault.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.recipevault.model.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}

