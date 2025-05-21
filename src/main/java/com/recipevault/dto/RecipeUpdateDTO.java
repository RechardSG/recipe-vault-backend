package com.recipevault.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecipeUpdateDTO {
    private String title;
    private String instructions;
    private String difficulty;   // should be one of: EASY, MEDIUM, HARD
    private String creatorName;
    private List<String> ingredients;
}
