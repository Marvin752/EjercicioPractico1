package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Lexer {

    // Palabras reservadas del lenguaje
    private static final Set<String> PALABRAS_RESERVADAS = Set.of(
            "if", "else", "for", "print", "int"
    );

    // Cadena base para generar combinaciones tipo "asdfg"
    private static final String CADENA_BASE = "asdfg";

    private final String input;
    private int pos;

    public Lexer(String input) {
        this.input = input;
        this.pos = 0;
    }

     // Genera todas las combinaciones posibles (subcadenas) de "asdfg"

    private Set<String> getCombinacionesAsdfg() {
        Set<String> combinaciones = new java.util.HashSet<>();
        int n = CADENA_BASE.length();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                combinaciones.add(CADENA_BASE.substring(i, j));
            }
        }

        return combinaciones;
    }

    /*
      Método principal del lexer
      Recorre todo el texto y va generando tokens
     */
    public List<Token> analizar() {
        List<Token> tokens = new ArrayList<>();

        while (pos < input.length()) {
            char c = input.charAt(pos);

            // Ignorar espacios en blanco
            if (Character.isWhitespace(c)) {
                pos++;
                continue;
            }

            // Letras → puede ser palabra reservada o identificador
            if (Character.isLetter(c)) {
                tokens.add(leerPalabraOIdentificador());
                continue;
            }

            // Números
            if (Character.isDigit(c)) {
                tokens.add(leerNumero());
                continue;
            }

            // Operador de asignación :=
            if (c == ':') {
                tokens.add(leerAsignacion());
                continue;
            }

            // Operadores relacionales
            if (c == '<' || c == '>' || c == '=') {
                tokens.add(leerOperadorRelacional());
                continue;
            }

            // Operadores aritméticos
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                tokens.add(new Token(Token.Tipo.OPERADOR_ARITMETICO, String.valueOf(c)));
                pos++;
                continue;
            }

            // Operador de rango ".."
            if (c == '.') {
                tokens.add(leerPunto());
                continue;
            }

            // Símbolos simples
            if ("{}[](),;".indexOf(c) >= 0) {
                tokens.add(new Token(Token.Tipo.SIMBOLO, String.valueOf(c)));
                pos++;
                continue;
            }

            // Si no reconoce el carácter, es error
            tokens.add(new Token(Token.Tipo.ERROR, "Carácter no reconocido: '" + c + "'"));
            pos++;
        }

        return tokens;
    }

    /*
      Lee palabras completas:
      - puede ser palabra reservada
      - puede ser identificador
      - o cadena tipo asdfg
     */
    private Token leerPalabraOIdentificador() {
        StringBuilder sb = new StringBuilder();

        while (pos < input.length() &&
                Character.isLetterOrDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            pos++;
        }

        String palabra = sb.toString();

        // Verificar si es palabra reservada
        if (PALABRAS_RESERVADAS.contains(palabra.toLowerCase())) {
            return new Token(Token.Tipo.PALABRA_RESERVADA, palabra.toLowerCase());
        }

        // Verificar si es combinación de "asdfg"
        Set<String> combinaciones = getCombinacionesAsdfg();
        if (combinaciones.contains(palabra.toLowerCase())) {
            return new Token(Token.Tipo.CADENA, palabra);
        }

        // Validar longitud del identificador
        if (palabra.length() > 10) {
            return new Token(Token.Tipo.ERROR,
                    "Identificador demasiado largo (>10): '" + palabra + "'");
        }

        return new Token(Token.Tipo.IDENTIFICADOR, palabra);
    }


      // Lee números enteros y valida que estén en el rango 0-100

    private Token leerNumero() {
        StringBuilder sb = new StringBuilder();

        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            pos++;
        }

        int valor = Integer.parseInt(sb.toString());

        if (valor < 0 || valor > 100) {
            return new Token(Token.Tipo.ERROR,
                    "Número fuera de rango (0-100): " + valor);
        }

        return new Token(Token.Tipo.NUMERO_ENTERO, sb.toString());
    }


      // Lee el operador de asignación :=

    private Token leerAsignacion() {
        if (pos + 1 < input.length() && input.charAt(pos + 1) == '=') {
            pos += 2;
            return new Token(Token.Tipo.OPERADOR_ASIGNACION, ":=");
        }

        pos++;
        return new Token(Token.Tipo.ERROR, "Carácter no reconocido: ':'");
    }

    /*
      Lee operadores relacionales como:
      >=, <=, <>, >, <, =
     */
    private Token leerOperadorRelacional() {
        char c = input.charAt(pos);

        if (pos + 1 < input.length()) {
            char siguiente = input.charAt(pos + 1);
            String doble = "" + c + siguiente;

            if (doble.equals(">=") || doble.equals("<=") || doble.equals("<>")) {
                pos += 2;
                return new Token(Token.Tipo.OPERADOR_RELACIONAL, doble);
            }
        }

        pos++;
        return new Token(Token.Tipo.OPERADOR_RELACIONAL, String.valueOf(c));
    }


     // Lee el operador de rango ".."
    private Token leerPunto() {
        if (pos + 1 < input.length() && input.charAt(pos + 1) == '.') {
            pos += 2;
            return new Token(Token.Tipo.OPERADOR_RELACIONAL, "..");
        }

        pos++;
        return new Token(Token.Tipo.ERROR, "Carácter no reconocido: '.'");
    }
}