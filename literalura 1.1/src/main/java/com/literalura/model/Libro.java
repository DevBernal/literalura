package com.literalura.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Libro {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String idioma;
    private Integer descargas;

    @ManyToOne(cascade = CascadeType.ALL)
    private Autor autor;
}
