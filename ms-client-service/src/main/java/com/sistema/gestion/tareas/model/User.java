package com.sistema.gestion.tareas.model;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name ="users", schema = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User  implements Serializable
{

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private Long id;

    @Column(unique = true, nullable = false)
    private String identificacion;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellidos;

    private Integer edad;

    private String cargo;


    //campos auditoria

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private Instant fechaCreacion;


    @Column(name = "usuario_creacion")
    private String usuarioCreacion;


    @Column(name = "fecha_modifica")
    private Instant fechaModifica;


    @Column(name = "usuario_modifica")
    private String usuarioModifica;

    @Column(name = "estado")
    private  Boolean estado;



}
