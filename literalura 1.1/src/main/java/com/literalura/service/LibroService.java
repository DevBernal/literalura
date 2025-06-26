package com.literalura.service;

import com.literalura.client.GutendexClient;
import com.literalura.client.dto.*;
import com.literalura.model.Autor;
import com.literalura.model.Libro;
import com.literalura.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroService {
    private final LibroRepository libroRepo;
    private final AutorRepository autorRepo;
    private final GutendexClient gutendex;

    public LibroService(LibroRepository libroRepo, AutorRepository autorRepo, GutendexClient gutendex) {
        this.libroRepo = libroRepo;
        this.autorRepo = autorRepo;
        this.gutendex = gutendex;
    }

    public void buscarYGuardarLibro(String titulo) {
        var respuesta = gutendex.buscarLibro(titulo);
        if (respuesta.results().isEmpty()) {
            System.out.println("❌ Libro no encontrado.");
            return;
        }

        var dto = respuesta.results().get(0);
        if (libroRepo.findByTitulo(dto.title()).isPresent()) {
            System.out.println("⚠️ El libro ya está registrado.");
            return;
        }

        var autorDto = dto.authors().isEmpty() ? new GutendexResponse.AutorDTO("Desconocido", null, null) : dto.authors().get(0);
        var autor = new Autor(null, autorDto.name(), autorDto.birth_year(), autorDto.death_year());

        var libro = new Libro(null, dto.title(), dto.languages().get(0), dto.download_count(), autor);
        libroRepo.save(libro);
        System.out.println("✅ Libro guardado: " + libro.getTitulo());
    }

    public void listarLibros() {
        var libros = libroRepo.findAll();

        if (libros.isEmpty()) {
            System.out.println("❌ No hay libros registrados.");
            return;
        }

        System.out.println("\n📚 Libros Registrados:");
        libros.forEach(libro -> {
            System.out.println("""
            -------------------------------
            📘 Título: %s
            🖋️ Autor: %s
            🌍 Idioma: %s
            ⬇️ Descargas: %d
            -------------------------------
            """.formatted(
                    libro.getTitulo(),
                    libro.getAutor().getNombre(),
                    libro.getIdioma().toUpperCase(),
                    libro.getDescargas()
            ));
        });
    }


    public void listarAutores() {
        var autores = autorRepo.findAll();

        if (autores.isEmpty()) {
            System.out.println("❌ No hay autores registrados.");
            return;
        }

        System.out.println("\n👤 Autores Registrados:");
        autores.forEach(autor -> {
            System.out.println("""
            -------------------------------
            👤 Nombre: %s
            🗓️ Nacimiento: %s
            ☠️ Fallecimiento: %s
            -------------------------------
            """.formatted(
                    autor.getNombre(),
                    autor.getNacimiento() != null ? autor.getNacimiento() : "Desconocido",
                    autor.getFallecimiento() != null ? autor.getFallecimiento() : "Desconocido"
            ));
        });
    }


    public void listarAutoresVivosEn(int anio) {
        var autores = autorRepo.findAll().stream()
                .filter(a -> a.getNacimiento() != null && a.getFallecimiento() != null
                        && a.getNacimiento() <= anio && a.getFallecimiento() >= anio)
                .toList();

        if (autores.isEmpty()) {
            System.out.println("❌ No se encontraron autores vivos en el año " + anio);
            return;
        }

        System.out.println("\n👥 Autores vivos en el año " + anio + ":");
        autores.forEach(autor -> {
            System.out.println("""
            -------------------------------
            👤 Nombre: %s
            🗓️ Nacimiento: %d
            ☠️ Fallecimiento: %d
            -------------------------------
            """.formatted(
                    autor.getNombre(),
                    autor.getNacimiento(),
                    autor.getFallecimiento()
            ));
        });
    }


    public void listarLibrosPorIdioma(String idioma) {
        libroRepo.findAll().stream()
                .filter(l -> l.getIdioma().equalsIgnoreCase(idioma))
                .forEach(l -> System.out.println("🌐 " + l.getTitulo()));

    }
}
