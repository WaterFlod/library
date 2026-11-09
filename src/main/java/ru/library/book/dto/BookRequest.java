package ru.library.book.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookRequest(
        @NotBlank @Size(max = 20) String isbn,
        @NotBlank String title,
        @NotBlank String author,
        @Min(1000) @Max(2100) int year
) {}
