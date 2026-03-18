package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Ruta por defecto del archivo
        String rutaArchivo = "codigo_fuente.txt";

        // Permite pasar otra ruta como argumento
        if (args.length > 0) {
            rutaArchivo = args[0];
        }

        String contenido;

        // Intentar leer el archivo
        try {
            contenido = Files.readString(Paths.get(rutaArchivo));
        } catch (IOException e) {
            System.err.println("No se pudo leer el archivo: " + rutaArchivo);
            return;
        }

        // Se crea el lexer y se analizan los tokens
        Lexer lexer = new Lexer(contenido);
        List<Token> tokens = lexer.analizar();

        // Se imprimen los resultados en forma de tabla
        GeneradorTabla generador = new GeneradorTabla();
        generador.imprimirTabla(tokens, rutaArchivo);
    }
}