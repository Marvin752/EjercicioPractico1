package org.example;

public class Token {

    // Tipos de tokens que el analizador puede reconocer
    public enum Tipo {
        IDENTIFICADOR,
        NUMERO_ENTERO,
        CADENA,
        OPERADOR_ARITMETICO,
        OPERADOR_ASIGNACION,
        OPERADOR_RELACIONAL,
        SIMBOLO,
        PALABRA_RESERVADA,
        ERROR
    }

    private Tipo tipo;
    private String valor;

    // Constructor: guarda el tipo de token y su valor
    public Token(Tipo tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    // Devuelve el tipo del token
    public Tipo getTipo() {
        return tipo;
    }

    // Devuelve el valor del token (lo que se leyó del texto)
    public String getValor() {
        return valor;
    }
}