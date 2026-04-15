package org.example;

import java.util.ArrayList;
import java.util.List;

public class Nodo {

    private String valor;
    private List<Nodo> hijos;

    public Nodo(String valor) {
        this.valor = valor;
        this.hijos = new ArrayList<>();
    }

    public void agregarHijo(Nodo hijo) {
        hijos.add(hijo);
    }

    public String getValor() {
        return valor;
    }

    public List<Nodo> getHijos() {
        return hijos;
    }

    // Imprime el árbol con estructura visual (ramas)
    public void imprimir(String prefijo, boolean esUltimo) {

        System.out.print(prefijo);

        if (esUltimo) {
            System.out.print("└── ");
            prefijo += "    ";
        } else {
            System.out.print("├── ");
            prefijo += "│   ";
        }

        System.out.println(valor);

        for (int i = 0; i < hijos.size(); i++) {
            hijos.get(i).imprimir(prefijo, i == hijos.size() - 1);
        }
    }
}