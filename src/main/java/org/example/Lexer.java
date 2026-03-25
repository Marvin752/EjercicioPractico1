package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    // Lista de palabras reservadas del lenguaje
    private static final Set<String> PALABRAS_RESERVADAS = Set.of(
            "if", "else", "for", "print", "int"
    );

    // Cadena base para generar combinaciones tipo "asdfg"
    private static final String CADENA_BASE = "asdfg";

    // ── Expresiones regulares que usa el lexer ─────────────────────────────
    // Números enteros (uno o más dígitos)
    private static final Pattern PATRON_NUMERO = Pattern.compile("\\d+");

    // Identificadores o palabras (empiezan con letra)
    private static final Pattern PATRON_PALABRA = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");

    // Operador de asignación
    private static final Pattern PATRON_ASIGNACION = Pattern.compile(":=");

    // Operadores relacionales dobles
    private static final Pattern PATRON_RELACIONAL_DOBLE = Pattern.compile(">=|<=|<>");

    // Operador de rango
    private static final Pattern PATRON_RANGO = Pattern.compile("\\.\\.");

    // Operadores aritméticos básicos
    private static final Pattern PATRON_ARITMETICO = Pattern.compile("[+\\-*/]");

    // Operadores relacionales simples
    private static final Pattern PATRON_RELACIONAL_SIMPLE = Pattern.compile("[><]|=(?!=)");

    // Símbolos permitidos
    private static final Pattern PATRON_SIMBOLO = Pattern.compile("[{}\\[\\](),;]");
    // ──────────────────────────────────────────────────────────────────────

    private final String input;
    private int pos;

    // Aquí se van guardando los errores que se encuentren
    private final List<ErrorLexico> errores = new ArrayList<>();

    public Lexer(String input) {
        this.input = input;
        this.pos = 0;
    }

    // Devuelve la lista de errores para que el Main los pueda mostrar
    public List<ErrorLexico> getErrores() {
        return errores;
    }

    // Genera todas las subcadenas posibles de "asdfg"
    private Set<String> getCombinacionesAsdfg() {
        Set<String> combinaciones = new java.util.HashSet<>();
        int n = CADENA_BASE.length();
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j <= n; j++)
                combinaciones.add(CADENA_BASE.substring(i, j));
        return combinaciones;
    }

    // Intenta hacer match de un patrón justo en la posición actual
    private Matcher matchEn(Pattern patron) {
        Matcher m = patron.matcher(input);
        m.region(pos, input.length()); // solo analiza desde donde vamos
        m.useAnchoringBounds(true);    // respeta ese límite como si fuera inicio real
        if (m.lookingAt()) return m;   // solo si coincide desde aquí mismo
        return null;
    }

    // Método principal: recorre todo el texto y va generando tokens
    public List<Token> analizar() {
        List<Token> tokens = new ArrayList<>();

        while (pos < input.length()) {
            char c = input.charAt(pos);

            // Ignora espacios en blanco
            if (Character.isWhitespace(c)) { pos++; continue; }

            // Primero intentamos el operador := (más específico)
            Matcher mAsig = matchEn(PATRON_ASIGNACION);
            if (mAsig != null) {
                tokens.add(new Token(Token.Tipo.OPERADOR_ASIGNACION, ":="));
                pos += 2;
                continue;
            }

            // Si hay ':' solo, es un error de asignación incompleta
            if (c == ':') {
                tokens.add(new Token(Token.Tipo.ERROR, ":"));
                errores.add(new ErrorLexico(TipoError.ASIGNACION_INCOMPLETA, ":"));
                pos++;
                continue;
            }

            // Operador de rango ".."
            Matcher mRango = matchEn(PATRON_RANGO);
            if (mRango != null) {
                tokens.add(new Token(Token.Tipo.OPERADOR_RELACIONAL, ".."));
                pos += 2;
                continue;
            }

            // Punto solo → error
            if (c == '.') {
                tokens.add(new Token(Token.Tipo.ERROR, "."));
                errores.add(new ErrorLexico(TipoError.PUNTO_INVALIDO, "."));
                pos++;
                continue;
            }

            // Operadores relacionales dobles (>=, <=, <>)
            Matcher mRelDoble = matchEn(PATRON_RELACIONAL_DOBLE);
            if (mRelDoble != null) {
                tokens.add(new Token(Token.Tipo.OPERADOR_RELACIONAL, mRelDoble.group()));
                pos += 2;
                continue;
            }

            // Operadores relacionales simples (>, <, =)
            Matcher mRelSimple = matchEn(PATRON_RELACIONAL_SIMPLE);
            if (mRelSimple != null) {
                tokens.add(new Token(Token.Tipo.OPERADOR_RELACIONAL, mRelSimple.group()));
                pos++;
                continue;
            }

            // Operadores aritméticos
            Matcher mArit = matchEn(PATRON_ARITMETICO);
            if (mArit != null) {
                tokens.add(new Token(Token.Tipo.OPERADOR_ARITMETICO, mArit.group()));
                pos++;
                continue;
            }

            // Símbolos como {}, (), etc.
            Matcher mSim = matchEn(PATRON_SIMBOLO);
            if (mSim != null) {
                tokens.add(new Token(Token.Tipo.SIMBOLO, mSim.group()));
                pos++;
                continue;
            }

            // Palabras (pueden ser reservadas, identificadores o cadenas tipo asdfg)
            Matcher mPal = matchEn(PATRON_PALABRA);
            if (mPal != null) {
                tokens.add(leerPalabraOIdentificador(mPal.group()));
                pos += mPal.group().length();
                continue;
            }

            // Números
            Matcher mNum = matchEn(PATRON_NUMERO);
            if (mNum != null) {
                tokens.add(leerNumero(mNum.group()));
                pos += mNum.group().length();
                continue;
            }

            // Si nada coincide, es un carácter inválido
            String charInvalido = String.valueOf(c);
            tokens.add(new Token(Token.Tipo.ERROR, charInvalido));
            errores.add(new ErrorLexico(TipoError.CARACTER_INVALIDO, charInvalido));
            pos++;
        }

        return tokens;
    }

    // Decide si una palabra es reservada, identificador o cadena especial
    private Token leerPalabraOIdentificador(String palabra) {

        // Si es palabra reservada
        if (PALABRAS_RESERVADAS.contains(palabra.toLowerCase()))
            return new Token(Token.Tipo.PALABRA_RESERVADA, palabra.toLowerCase());

        // Si es parte de las combinaciones de "asdfg"
        if (getCombinacionesAsdfg().contains(palabra.toLowerCase()))
            return new Token(Token.Tipo.CADENA, palabra);

        // Si es demasiado larga → error
        if (palabra.length() > 10) {
            errores.add(new ErrorLexico(TipoError.IDENTIFICADOR_LARGO, palabra));
            return new Token(Token.Tipo.ERROR, palabra);
        }

        // Si no, es un identificador normal
        return new Token(Token.Tipo.IDENTIFICADOR, palabra);
    }

    // Valida que el número esté en el rango permitido
    private Token leerNumero(String lexema) {
        int valor = Integer.parseInt(lexema);

        if (valor < 0 || valor > 100) {
            errores.add(new ErrorLexico(TipoError.NUMERO_FUERA_RANGO, lexema));
            return new Token(Token.Tipo.ERROR, lexema);
        }

        return new Token(Token.Tipo.NUMERO_ENTERO, lexema);
    }
}