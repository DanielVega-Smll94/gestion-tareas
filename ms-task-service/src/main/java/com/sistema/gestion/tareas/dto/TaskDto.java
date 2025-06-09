package com.sistema.gestion.tareas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sistema.gestion.tareas.util.EstadoTarea;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto implements Serializable {

    private Long id;

    private String codigoTarea;

    @NotBlank
    private String titulo;

    private String descripcion;

    private String criteriosAceptacion;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private Integer tiempoDesarrollo; // en horas


    private String estadoTarea;

    private Long usuarioIdAsignado;

    //campos auditoria

    private Instant fechaCreacion;

    private String usuarioCreacion;

    private Instant fechaModifica;

    private String usuarioModifica;

    private  Boolean activo = true;;
}