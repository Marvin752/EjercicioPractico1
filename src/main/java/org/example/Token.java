package org.example;

public class Token {


    //Tipos posibles de tokens que puede reconocer el analizador léxico

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

    //Constructor del token

    public Token(Tipo tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }
}