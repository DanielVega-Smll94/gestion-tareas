package com.sistema.gestion.tareas.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EstadoTarea {
    BACKLOG("Backlog"),
    DOING("Doing"),
    IN_REVIEW("In Review"),
    DONE("Done");

    private final String label;

    EstadoTarea(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static EstadoTarea fromLabel(String value) {
        for (EstadoTarea estado : values()) {
            if (estado.label.equalsIgnoreCase(value)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado inválido: " + value);
    }
}