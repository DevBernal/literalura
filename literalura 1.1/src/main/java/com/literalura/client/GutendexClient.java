package com.literalura.client;

import com.literalura.client.dto.GutendexResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GutendexClient {
    private final String URL = "https://gutendex.com/books/?search=";
    private final RestTemplate restTemplate = new RestTemplate();

    public GutendexResponse buscarLibro(String titulo) {
        String urlFormateada = URL + titulo.replace(" ", "+");
        return restTemplate.getForObject(urlFormateada, GutendexResponse.class);
    }
}

