package com.oo2.grupo3.helpers.exceptions;

public class DiaNoEncontradoException extends RuntimeException {
    private final String fecha;

    public DiaNoEncontradoException(String fecha) {
        super("Día no encontrado con fecha: " + fecha);
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }
}
