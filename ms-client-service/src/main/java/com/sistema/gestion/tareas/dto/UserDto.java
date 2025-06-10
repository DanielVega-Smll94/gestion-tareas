package com.sistema.gestion.tareas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.io.Serializable;
import java.util.UUID;



@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements Serializable
{

    private long id;

    @NotBlank
    private String identificacion;

    @NotBlank
    private String nombres;

    @NotBlank
    private String apellidos;

    @Min(18)
    private Integer edad;

    @NotBlank
    private String cargo;

    private boolean estado;



}