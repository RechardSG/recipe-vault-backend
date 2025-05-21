package com.recipevault.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class RecipeResponseDTO {
    // Getters and Setters
    private Long id;
    private String title;
    private String instructions;
    private String difficulty;
    private String creatorName;
    private LocalDateTime createdDate;
    private List<String> ingredients;
    public void setImageUrl(String imageUrl) {
    }
}
