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

    // Devuelve el token actual
    private Token actual() {
        if (posicion < tokens.size()) {
            return tokens.get(posicion);
        }
        return null;
    }

    // Avanza al siguiente token
    private void avanzar() {
        posicion++;
    }

    // Registra un error sin detener la ejecución
    private void error(String mensaje) {
        String tokenActual = (actual() != null) ? actual().getValor() : "EOF";
        errores.add(new ErrorSintactico(mensaje, tokenActual));

        // Se avanza para evitar quedarse en el mismo token
        avanzar();
    }

    public List<ErrorSintactico> getErrores() {
        return errores;
    }

    // Método principal que construye el árbol
    public Nodo parse() {
        Nodo raiz = new Nodo("PROGRAMA");

        while (actual() != null) {
            Nodo sentencia = parseSentencia();
            if (sentencia != null) {
                raiz.agregarHijo(sentencia);
            }
        }

        return raiz;
    }

    // Analiza una sentencia
    private Nodo parseSentencia() {

        if (actual() == null) return null;

        // Estructura if
        if (actual().getValor().equalsIgnoreCase("if")) {
            return parseIf();
        }

        // Instrucción print
        else if (actual().getValor().equalsIgnoreCase("print")) {
            Nodo nodo = new Nodo("PRINT");

            avanzar();
            nodo.agregarHijo(parseExpresion());

            if (actual() != null && actual().getValor().equals(";")) {
                avanzar();
            } else {
                error("Se esperaba ';'");
            }

            return nodo;
        }

        // Asignación (identificador := expresión)
        else if (actual().getTipo() == Token.Tipo.IDENTIFICADOR) {
            Nodo nodo = new Nodo("ASIGNACION");

            nodo.agregarHijo(new Nodo(actual().getValor()));
            avanzar();

            if (actual() != null && actual().getValor().equals(":=")) {
                avanzar();
                nodo.agregarHijo(parseExpresion());

                if (actual() != null && actual().getValor().equals(";")) {
                    avanzar();
                } else {
                    error("Se esperaba ';'");
                }
            } else {
                error("Se esperaba ':='");
            }

            return nodo;
        }

        // Caso no válido
        else {
            error("Sentencia no válida");
            return new Nodo("ERROR");
        }
    }

    // Estructura if completa
    private Nodo parseIf() {
        Nodo nodo = new Nodo("IF");

        avanzar(); // consume "if"

        if (actual() != null && actual().getValor().equals("(")) {
            avanzar();
            nodo.agregarHijo(parseExpresion());

            if (actual() != null && actual().getValor().equals(")")) {
                avanzar();
            } else {
                error("Se esperaba ')'");
            }

            if (actual() != null && actual().getValor().equals("{")) {
                avanzar();

                Nodo bloque = new Nodo("BLOQUE");

                // Se procesan múltiples sentencias dentro del bloque
                while (actual() != null && !actual().getValor().equals("}")) {
                    bloque.agregarHijo(parseSentencia());
                }

                if (actual() != null && actual().getValor().equals("}")) {
                    avanzar();
                } else {
                    error("Se esperaba '}'");
                }

                nodo.agregarHijo(bloque);

            } else {
                error("Se esperaba '{'");
            }

        } else {
            error("Se esperaba '(' después de if");
        }

        return nodo;
    }

    // Expresión simple
    private Nodo parseExpresion() {
        Nodo nodo = new Nodo("EXPRESION");

        nodo.agregarHijo(parseTermino());

        if (actual() != null) {

            if (actual().getTipo() == Token.Tipo.OPERADOR_ARITMETICO ||
                    actual().getTipo() == Token.Tipo.OPERADOR_RELACIONAL) {

                nodo.agregarHijo(new Nodo(actual().getValor()));
                avanzar();

                nodo.agregarHijo(parseTermino());
            }

            // Permite usar := dentro de expresiones
            else if (actual().getValor().equals(":=")) {
                nodo.agregarHijo(new Nodo(":="));
                avanzar();
                nodo.agregarHijo(parseTermino());
            }
        }

        return nodo;
    }

    // Parte básica de una expresión
    private Nodo parseTermino() {

        if (actual() == null) {
            error("Expresión incompleta");
            return new Nodo("ERROR");
        }

        Nodo nodo = new Nodo("TERMINO");

        if (actual().getTipo() == Token.Tipo.IDENTIFICADOR ||
                actual().getTipo() == Token.Tipo.NUMERO_ENTERO) {

            nodo.agregarHijo(new Nodo(actual().getValor()));
            avanzar();
        } else {
            error("Se esperaba identificador o número");
            return new Nodo("ERROR");
        }

        return nodo;
    }
}