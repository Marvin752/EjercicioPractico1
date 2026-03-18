package org.example;

import java.util.List;

public class GeneradorTabla {


      /* Este método se encarga de mostrar los tokens en forma de tabla.
      Solo es para presentación, no afecta el análisis léxico. */

    public void imprimirTabla(List<Token> tokens, String nombreArchivo) {

        int anchoN     = 6;
        int anchoTipo  = 30;
        int anchoValor = 65;

        String separador = "+" + "-".repeat(anchoN) + "+" + "-".repeat(anchoTipo) + "+" + "-".repeat(anchoValor) + "+";

        System.out.println("\n=== Analizador Léxico — " + nombreArchivo + " ===\n");
        System.out.println(separador);

        System.out.printf("| %-" + (anchoN-1)     + "s| %-" + (anchoTipo-1)  + "s| %-" + (anchoValor-1) + "s|%n",
                "#", "TIPO", "VALOR");

        System.out.println(separador);

        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            System.out.printf("| %-" + (anchoN-1)     + "s| %-" + (anchoTipo-1)  + "s| %-" + (anchoValor-1) + "s|%n",
                    (i + 1),
                    t.getTipo(),
                    t.getValor());
        }

        System.out.println(separador);
        System.out.println("  Total de tokens: " + tokens.size() + "\n");
    }
}