package org.example;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private List<Token> tokens;
    private int posicion;
    private List<ErrorSintactico> errores = new ArrayList<>();

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.posicion = 0;
    }

    private Token actual() {
        return (posicion < tokens.size()) ? tokens.get(posicion) : null;
    }

    private void avanzar() {
        posicion++;
    }

    public List<ErrorSintactico> getErrores() {
        return errores;
    }

    private void error(String msg) {
        String t = (actual() != null) ? actual().getValor() : "EOF";
        errores.add(new ErrorSintactico(msg, t));
        avanzar();
    }

    // =========================
    // PROGRAMA
    // =========================
    public Nodo parse() {
        Nodo raiz = new Nodo("PROGRAMA");

        Nodo lista = new Nodo("LISTA_SENTENCIAS");

        while (actual() != null) {
            lista.agregarHijo(parseSentencia());
        }

        raiz.agregarHijo(lista);
        return raiz;
    }

    // =========================
    // SENTENCIAS
    // =========================
    private Nodo parseSentencia() {

        if (actual() == null) return new Nodo("ERROR");

        if (actual().getValor().equalsIgnoreCase("if")) return parseIf();

        if (actual().getValor().equalsIgnoreCase("print")) return parsePrint();

        if (actual().getTipo() == Token.Tipo.IDENTIFICADOR) return parseAsignacion();

        error("Sentencia no válida");
        return new Nodo("ERROR");
    }

    // =========================
    // ASIGNACION
    // =========================
    private Nodo parseAsignacion() {

        Nodo nodo = new Nodo("ASIGNACION");

        nodo.agregarHijo(parseIdentificador());

        if (actual() != null && actual().getValor().equals(":=")) {
            nodo.agregarHijo(new Nodo(":="));
            avanzar();
        } else {
            error("Se esperaba ':='");
        }

        nodo.agregarHijo(parseExpresion());

        if (actual() != null && actual().getValor().equals(";")) {
            nodo.agregarHijo(new Nodo(";"));
            avanzar();
        } else {
            error("Se esperaba ';'");
        }

        return nodo;
    }

    // =========================
    // PRINT
    // =========================
    private Nodo parsePrint() {

        Nodo nodo = new Nodo("PRINT");
        avanzar();

        nodo.agregarHijo(parseExpresion());

        if (actual() != null && actual().getValor().equals(";")) {
            nodo.agregarHijo(new Nodo(";"));
            avanzar();
        } else {
            error("Se esperaba ';'");
        }

        return nodo;
    }

    // =========================
    // IF
    // =========================
    private Nodo parseIf() {

        Nodo nodo = new Nodo("IF");
        avanzar();

        if (actual() != null && actual().getValor().equals("(")) {
            avanzar();
            nodo.agregarHijo(parseExpresion());

            if (actual() != null && actual().getValor().equals(")")) {
                avanzar();
            } else error("Se esperaba ')'");

            if (actual() != null && actual().getValor().equals("{")) {
                avanzar();

                Nodo bloque = new Nodo("LISTA_SENTENCIAS");

                while (actual() != null && !actual().getValor().equals("}")) {
                    bloque.agregarHijo(parseSentencia());
                }

                if (actual() != null && actual().getValor().equals("}")) {
                    avanzar();
                } else error("Se esperaba '}'");

                nodo.agregarHijo(bloque);

            } else error("Se esperaba '{'");
        } else error("Se esperaba '('");

        return nodo;
    }

    // =========================
    // EXPRESION
    // =========================
    private Nodo parseExpresion() {

        Nodo nodo = new Nodo("EXPRESION");

        nodo.agregarHijo(parseTermino());

        if (actual() != null) {

            if (actual().getTipo() == Token.Tipo.OPERADOR_ARITMETICO) {
                nodo.agregarHijo(new Nodo(actual().getValor()));
                avanzar();
                nodo.agregarHijo(parseTermino());
            }

            else if (actual().getTipo() == Token.Tipo.OPERADOR_RELACIONAL) {
                nodo.agregarHijo(new Nodo(actual().getValor()));
                avanzar();
                nodo.agregarHijo(parseTermino());
            }
        }

        return nodo;
    }

    // =========================
    // TERMINO (AQUÍ ESTABA EL CAMBIO IMPORTANTE)
    // =========================
    private Nodo parseTermino() {

        if (actual() == null) return new Nodo("ERROR");

        Nodo nodo = new Nodo("TERMINO");

        if (actual().getTipo() == Token.Tipo.IDENTIFICADOR) {
            nodo.agregarHijo(parseIdentificador());
        }

        else if (actual().getTipo() == Token.Tipo.NUMERO_ENTERO) {
            nodo.agregarHijo(parseNumero());
        }

        else if (actual().getTipo() == Token.Tipo.CADENA) {
            nodo.agregarHijo(new Nodo("CADENA -> " + actual().getValor()));
            avanzar();
        }

        else {
            error("Termino inválido");
            return new Nodo("ERROR");
        }

        return nodo;
    }

    // =========================
    // IDENTIFICADOR
    // =========================
    private Nodo parseIdentificador() {

        Nodo nodo = new Nodo("IDENTIFICADOR");

        String val = actual().getValor();
        avanzar();

        char[] chars = val.toCharArray();

        nodo.agregarHijo(new Nodo("LETRA -> " + chars[0]));

        if (chars.length > 1) {
            Nodo resto = new Nodo("RESTO");

            for (int i = 1; i < chars.length; i++) {
                resto.agregarHijo(new Nodo("LETRA -> " + chars[i]));
            }

            nodo.agregarHijo(resto);
        }

        return nodo;
    }

    // =========================
    // NUMERO
    // =========================
    private Nodo parseNumero() {

        Nodo nodo = new Nodo("NUMERO");

        char[] chars = actual().getValor().toCharArray();
        avanzar();

        for (char c : chars) {
            nodo.agregarHijo(new Nodo("DIGITO -> " + c));
        }

        return nodo;
    }
}