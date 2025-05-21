package com.recipevault.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RecipeCreateDTO {
    private String title;
    private String instructions;
    private String difficulty;
    private String creatorName;
    private List<String> ingredients;

}
