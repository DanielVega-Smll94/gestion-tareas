package com.sistema.gestion.tareas.service;

import com.sistema.gestion.tareas.dto.TaskDto;
import com.sistema.gestion.tareas.model.Task;
import com.sistema.gestion.tareas.util.EstadoTarea;
import com.sistema.gestion.tareas.util.GenericResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface TaskService {


    GenericResponse findAllTask();

    GenericResponse findByIdTask(long id);

    @Transactional(rollbackFor = {RuntimeException.class} )
    GenericResponse saveUpdateTask(TaskDto taskS);

    GenericResponse deleteTask(Long id);

    GenericResponse actualizarEstado(Long id, String nuevoEstado);

    GenericResponse asignarTask(String codigoTarea, long idUsuario);

    GenericResponse tareasPorUsuario(long idUsuario);

    boolean validarUsuario(long id);
}