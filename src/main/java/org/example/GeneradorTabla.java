package org.example;

import java.util.List;

public class GeneradorTabla {

    // Imprime la tabla con todos los tokens encontrados
    public void imprimirTabla(List<Token> tokens, String nombreArchivo) {

        int anchoN     = 6;
        int anchoTipo  = 30;
        int anchoValor = 65;

        // Línea separadora de la tabla
        String sep = "+" + "-".repeat(anchoN)
                + "+" + "-".repeat(anchoTipo)
                + "+" + "-".repeat(anchoValor) + "+";

        System.out.println("\n=== Analizador Léxico — " + nombreArchivo + " ===\n");
        System.out.println(sep);

        // Encabezados de la tabla
        System.out.printf("| %-" + (anchoN-1)    + "s| %-" + (anchoTipo-1)
                + "s| %-" + (anchoValor-1) + "s|%n", "#", "TIPO", "VALOR");
        System.out.println(sep);

        // Recorre todos los tokens y los imprime fila por fila
        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            System.out.printf("| %-" + (anchoN-1)    + "s| %-" + (anchoTipo-1)
                            + "s| %-" + (anchoValor-1) + "s|%n",
                    (i + 1), t.getTipo(), t.getValor());
        }

        System.out.println(sep);
        System.out.println("  Total de tokens: " + tokens.size() + "\n");
    }

    // Imprime la tabla de errores léxicos encontrados
    public void imprimirTablaErrores(List<ErrorLexico> errores) {

        int anchoN    = 6;
        int anchoTipo = 30;
        int anchoVal  = 25;
        int anchoDesc = 55;

        // Línea separadora de la tabla
        String sep = "+" + "-".repeat(anchoN)
                + "+" + "-".repeat(anchoTipo)
                + "+" + "-".repeat(anchoVal)
                + "+" + "-".repeat(anchoDesc) + "+";

        System.out.println("=== Tabla de Errores Léxicos ===\n");

        // Si no hay errores, solo mostramos el mensaje y salimos
        if (errores.isEmpty()) {
            System.out.println("  No se encontraron errores léxicos.\n");
            return;
        }

        System.out.println(sep);

        // Encabezados de la tabla de errores
        System.out.printf("| %-" + (anchoN-1)    + "s| %-" + (anchoTipo-1)
                        + "s| %-" + (anchoVal-1)  + "s| %-" + (anchoDesc-1) + "s|%n",
                "#", "TIPO DE ERROR", "VALOR", "DESCRIPCIÓN");
        System.out.println(sep);

        // Recorre los errores y los imprime
        for (int i = 0; i < errores.size(); i++) {
            ErrorLexico e = errores.get(i);
            System.out.printf("| %-" + (anchoN-1)    + "s| %-" + (anchoTipo-1)
                            + "s| %-" + (anchoVal-1)  + "s| %-" + (anchoDesc-1) + "s|%n",
                    (i + 1),
                    e.getNombreTipo(),
                    e.getValor(),
                    e.getDescripcion());
        }

        System.out.println(sep);
        System.out.println("  Total de errores: " + errores.size() + "\n");
    }
}