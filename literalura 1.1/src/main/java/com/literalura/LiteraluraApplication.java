package com.literalura;

import com.literalura.service.LibroService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	private final LibroService servicio;

	public LiteraluraApplication(LibroService servicio) {
		this.servicio = servicio;
	}

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Scanner scanner = new Scanner(System.in);
		int opcion = -1;

		do {
			System.out.println("""
            \n📚 LITERALURA - Catálogo de Libros
            1. Buscar libro por título
            2. Listar libros registrados
            3. Listar autores registrados
            4. Listar autores vivos en un año
            5. Listar libros por idioma
            6. Exportar catálogo de libros a .txt
            0. Salir
            """);
			System.out.print("Seleccione una opción: ");

			try {
				opcion = Integer.parseInt(scanner.nextLine());

				switch (opcion) {
					case 1 -> {
						System.out.print("Título del libro: ");
						String titulo = scanner.nextLine();
						servicio.buscarYGuardarLibro(titulo);
					}
					case 2 -> servicio.listarLibros();
					case 3 -> servicio.listarAutores();
					case 4 -> {
						System.out.print("Ingrese el año: ");
						int anio = Integer.parseInt(scanner.nextLine());
						servicio.listarAutoresVivosEn(anio);
					}
					case 5 -> {
						System.out.print("Idioma (ES, EN, FR, PT): ");
						String idioma = scanner.nextLine();
						servicio.listarLibrosPorIdioma(idioma);
					}
					case 0 -> {
						System.out.println("👋 ¡Hasta luego!");
						System.exit(0);
					}
					default -> System.out.println("❌ Opción inválida. Intente nuevamente.");
				}
			} catch (NumberFormatException e) {
				System.out.println("⚠️ Entrada no válida. Por favor, escriba un número.");
			}
		} while (true);
	}
}
