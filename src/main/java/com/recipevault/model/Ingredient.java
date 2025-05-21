package com.recipevault.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    @JsonBackReference
    private Recipe recipe;

    public Ingredient() {}

    public Ingredient(String name, Recipe recipe) {
        this.name = name;
        this.recipe = recipe;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Recipe getRecipe() { return recipe; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }
}
