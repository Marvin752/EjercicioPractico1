package org.example;

public class ErrorSintactico {

    private String mensaje;
    private String token;

    public ErrorSintactico(String mensaje, String token) {
        this.mensaje = mensaje;
        this.token = token;
    }

    @Override
    public String toString() {
        return "Error: " + mensaje + " en token: " + token;
    }
}