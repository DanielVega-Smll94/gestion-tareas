package com.sistema.gestion.tareas.model;

import com.sistema.gestion.tareas.util.EstadoTarea;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name ="tasks", schema = "task")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_tarea", unique = true, nullable = false)
    private String codigoTarea;

    @NotBlank
    private String titulo;

    private String descripcion;

    private String criteriosAceptacion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_final")
    private LocalDate fechaFin;

    private Integer tiempoDesarrollo; // en horas

    @Column(name = "estado_tarea")
    private String estadoTarea;



    private Long usuarioIdAsignado;


    //campos auditoria

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private Instant fechaCreacion;


    @Column(name = "usuario_creacion")
    private String usuarioCreacion;


    @Column(name = "fecha_modifica")
    private Instant fechaModifica;


    @Column(name = "usuario_modifica")
    private String usuarioModifica;

    @Column(name = "activo")
    private  Boolean activo = true;;

    public Task(Long id, String codigoTarea) {
        this.id = id;
        this.codigoTarea = codigoTarea;
    }
}