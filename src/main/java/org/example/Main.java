package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Lista de archivos a procesar
        String[] archivos = {
                "codigo_fuente.txt",
                "entrada1.txt",
                "entrada2.txt",
                "entrada3.txt",
                "entrada4.txt"
        };

        for (String rutaArchivo : archivos) {

            System.out.println("\n========================================");
            System.out.println("📂 Analizando archivo: " + rutaArchivo);
            System.out.println("========================================");

            String contenido;

            try {
                contenido = Files.readString(Paths.get(rutaArchivo));
            } catch (IOException e) {
                System.err.println("No se pudo leer el archivo: " + rutaArchivo);
                continue;
            }

            // Analizador léxico
            Lexer lexer = new Lexer(contenido);
            List<Token> tokens = lexer.analizar();
            List<ErrorLexico> erroresLexicos = lexer.getErrores();

            // Mostrar tokens
            GeneradorTabla generador = new GeneradorTabla();
            generador.imprimirTabla(tokens, rutaArchivo);

            // Mostrar errores léxicos
            generador.imprimirTablaErrores(erroresLexicos);

            // Analizador sintáctico
            Parser parser = new Parser(tokens);
            Nodo arbol = parser.parse();

            System.out.println("\n🌳 ÁRBOL DE DERIVACIÓN:");
            arbol.imprimir("", true);

            List<ErrorSintactico> erroresSintacticos = parser.getErrores();

            // Resultado sintáctico
            if (erroresSintacticos.isEmpty()) {
                System.out.println("\n\u001B[32m✅ Sin errores sintácticos\u001B[0m");
            } else {
                System.out.println("\n❌ Errores sintácticos:");
                for (ErrorSintactico e : erroresSintacticos) {
                    System.out.println("\u001B[31m⚠ " + e + "\u001B[0m");
                }
            }
        }

        System.out.println("\n🎯 Análisis finalizado para todos los archivos.");
    }
}