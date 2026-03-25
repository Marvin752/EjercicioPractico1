package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Ruta por defecto del archivo que se va a analizar
        String rutaArchivo = "codigo_fuente.txt";

        // Si se manda una ruta por parámetros, usamos esa en lugar de la default
        if (args.length > 0) {
            rutaArchivo = args[0];
        }

        String contenido;

        // Intentamos leer todo el archivo como texto
        try {
            contenido = Files.readString(Paths.get(rutaArchivo));
        } catch (IOException e) {
            // Si falla, mostramos mensaje y terminamos el programa
            System.err.println("No se pudo leer el archivo: " + rutaArchivo);
            return;
        }

        // Creamos el lexer y analizamos el contenido
        Lexer lexer = new Lexer(contenido);
        List<Token> tokens = lexer.analizar();
        List<ErrorLexico> errores = lexer.getErrores();

        // Mostramos la tabla de tokens encontrados
        GeneradorTabla generador = new GeneradorTabla();
        generador.imprimirTabla(tokens, rutaArchivo);

        // Mostramos la tabla de errores (si hay)
        generador.imprimirTablaErrores(errores);
    }
}