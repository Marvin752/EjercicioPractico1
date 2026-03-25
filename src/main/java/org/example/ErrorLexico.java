package org.example;

public class ErrorLexico {

    private final TipoError tipoError;
    private final String valor;

    // Constructor: guarda el tipo de error y el valor que lo provocó
    public ErrorLexico(TipoError tipoError, String valor) {
        this.tipoError = tipoError;
        this.valor = valor;
    }

    // Devuelve el tipo de error (el enum)
    public TipoError getTipoError() {
        return tipoError;
    }

    // Devuelve el valor que causó el error (ej: "101", ":", etc.)
    public String getValor() {
        return valor;
    }

    // Obtiene la descripción del error desde el enum (para no repetir lógica aquí)
    public String getDescripcion() {
        return tipoError.getDescripcion();
    }

    // Obtiene el nombre corto del error para mostrarlo en la tabla
    public String getNombreTipo() {
        return tipoError.getNombre();
    }
}