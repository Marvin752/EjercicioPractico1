package org.example;

public enum TipoError {

    NUMERO_FUERA_RANGO(
            "Número fuera de rango",
            "Los números deben estar entre 0 y 100"
    ),
    IDENTIFICADOR_LARGO(
            "Identificador demasiado largo",
            "Los identificadores no pueden superar 10 caracteres"
    ),
    CARACTER_INVALIDO(
            "Carácter no reconocido",
            "El carácter no pertenece al alfabeto del lenguaje"
    ),
    ASIGNACION_INCOMPLETA(
            "Asignación incompleta",
            "Se esperaba '=' después de ':' para formar ':='"
    ),
    PUNTO_INVALIDO(
            "Punto inválido",
            "Se esperaba '..' para el operador de rango"
    );

    // Nombre corto del error (el que se muestra en la tabla)
    private final String nombre;

    // Descripción más detallada para entender qué pasó
    private final String descripcion;

    // Constructor del enum, aquí se asignan los valores a cada tipo de error
    TipoError(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Devuelve el nombre corto del error
    public String getNombre() {
        return nombre;
    }

    // Devuelve la descripción completa del error
    public String getDescripcion() {
        return descripcion;
    }
}