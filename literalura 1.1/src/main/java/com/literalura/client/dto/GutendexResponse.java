package com.literalura.client.dto;

import java.util.List;

public record GutendexResponse(List<LibroDTO> results) {

    public record LibroDTO(
            String title,
            List<String> languages,
            int download_count,
            List<AutorDTO> authors
    ) {
    }

    public record AutorDTO(
            String name,
            Integer birth_year,
            Integer death_year
    ) {
    }

}
