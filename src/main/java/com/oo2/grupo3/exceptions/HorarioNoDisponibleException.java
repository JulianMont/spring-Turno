package com.oo2.grupo3.exceptions;

public class HorarioNoDisponibleException extends RuntimeException {
    public HorarioNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}