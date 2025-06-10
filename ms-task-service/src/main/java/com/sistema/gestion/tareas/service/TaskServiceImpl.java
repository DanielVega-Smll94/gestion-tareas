package com.sistema.gestion.tareas.service;

import com.sistema.gestion.tareas.dto.TaskDto;
import com.sistema.gestion.tareas.mapper.TaskMapper;
import com.sistema.gestion.tareas.model.Task;
import com.sistema.gestion.tareas.repository.TaskRepository;
import com.sistema.gestion.tareas.util.EstadoTarea;
import com.sistema.gestion.tareas.util.GenericExceptionUtils;
import com.sistema.gestion.tareas.util.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Status;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService
{

    private final TaskRepository taskRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    TaskMapper taskMapper;

   /* @Value("${queue.validar-usuario}")
    private String validarUsuarioQueue;*/



    @Override
    public GenericResponse findAllTask()
    {
        GenericResponse genericResponse  =new GenericResponse();
        taskRepository.findAllByActivoTrue().ifPresentOrElse(taskList ->
        {
            List<TaskDto> taskDtoList = taskMapper.toTaskDtoList(taskList);
            genericResponse.data(taskDtoList)
                    .success(true)
                    .code(Status.OK.getStatusCode())
                    .message("Consulta Existosa");

        }, () -> {

            throw new GenericExceptionUtils("No se encontraron Datos", Status.OK);
        });
        return genericResponse;
    }


    @Override
    public GenericResponse findByIdTask(long id)
    {
        GenericResponse genericResponse  =new GenericResponse();
        taskRepository.findByActivoTrueAndId(id).ifPresentOrElse(task ->
        {
            TaskDto taskDto = taskMapper.taskToTaskDto(task);
            genericResponse.data(taskDto)
                    .success(true)
                    .code(Status.OK.getStatusCode())
                    .message("Consulta Existosa");

        }, () -> {
            genericResponse.success(false)
                    .code(Status.NOT_FOUND.getStatusCode())
                    .message("No se encontraron Datos");
        });

        return genericResponse;
    }


    @Override
    @Transactional(rollbackFor = {RuntimeException.class} )
    public GenericResponse saveUpdateTask(TaskDto taskS)
    {
        GenericResponse genericResponse  =new GenericResponse();

        taskRepository.
                findByActivoTrueAndId(Objects.requireNonNullElse(taskS.getId(), 0L))
                .ifPresentOrElse(task ->
                {
                    Task taskMap = taskMapper.taskDtoToTask(taskS);
                    taskMap.setId(task.getId());
                    taskMap.setFechaModifica(Instant.now());
                    taskMap.setFechaCreacion(task.getFechaCreacion());
                    taskMap.setUsuarioCreacion(task.getUsuarioCreacion());
                    taskMap.setActivo(task.getActivo());
                    genericResponse.data(taskMapper.taskToTaskDto(taskRepository.save(taskMap)))
                            .success(true)
                            .code(Status.OK.getStatusCode())
                            .message("Modificación Existosa");

                }, () ->
                {
                    Optional<Task> taskOptional =taskRepository.findByActivoTrueAndCodigoTarea(Objects.requireNonNullElse(taskS.getCodigoTarea(), ""));

                    if (taskOptional.isPresent())
                    {
                        genericResponse
                                .success(false)
                                .code(Status.BAD_REQUEST.getStatusCode())
                                .message("EL Codigo de la tarea : "+taskOptional.get().getCodigoTarea() +" ya se encuentra registrada"  );
                    }
                    else
                    {
                        taskS.setActivo(true);
                        Task taskSave= taskRepository.save(taskMapper.taskDtoToTask(taskS));
                        genericResponse.data(taskMapper.taskToTaskDto(taskSave))
                                .success(true)
                                .code(Status.CREATED.getStatusCode())
                                .message("Guardado Existosa");
                    }

                });

        return genericResponse;
    }

    @Override
    public GenericResponse deleteTask(Long id)
    {
        GenericResponse genericResponse  =new GenericResponse();

        taskRepository.findByActivoTrueAndId(id).ifPresentOrElse(task ->
        {
            task.setFechaModifica(Instant.now());
            task.setActivo(false);
            Task taskSave= taskRepository.save(task);
            genericResponse.data(taskMapper.taskToTaskDto(taskSave))
                    .success(true)
                    .code(Status.OK.getStatusCode())
                    .message("Elimino Existosamente");

        }, () ->
        {
            genericResponse.success(false)
                    .code(Status.NOT_FOUND.getStatusCode())
                    .message("No se Elimino");
        });

        return genericResponse;
    }




    @Override
    public GenericResponse actualizarEstado(Long id, String nuevoEstado)
    {
        GenericResponse genericResponse  =new GenericResponse();


        if (  nuevoEstado.equalsIgnoreCase(EstadoTarea.BACKLOG.getLabel()) ||
                nuevoEstado.equalsIgnoreCase(EstadoTarea.DOING.getLabel()) ||
                (nuevoEstado.equalsIgnoreCase(EstadoTarea.IN_REVIEW.getLabel())) ||
                nuevoEstado.equalsIgnoreCase(EstadoTarea.DONE.getLabel())  )
        {

        }else {
            return    genericResponse
                    .code(Status.BAD_REQUEST.getStatusCode())
                    .success(false)
                    .message("No se puede asignar una Estado no valido "+nuevoEstado);
        }

        taskRepository.findByActivoTrueAndId(id).ifPresentOrElse(task ->
        {
            task.setFechaModifica(Instant.now());
            task.setEstadoTarea(nuevoEstado);
            Task taskSave= taskRepository.save(task);
            genericResponse.data(taskMapper.taskToTaskDto(taskSave))
                    .success(true)
                    .code(Status.OK.getStatusCode())
                    .message("Se Actualizo el Estado  "+nuevoEstado+"  Existosamente");

        }, () ->
        {
            genericResponse.success(false)
                    .code(Status.NOT_FOUND.getStatusCode())
                    .message("No se Actualizo el Estado  "+nuevoEstado);

        });

        return genericResponse;
    }


    @Override
    public GenericResponse asignarTask(String codigoTarea, long idUsuario) {
        GenericResponse genericResponse  =new GenericResponse();
        //Optional<Task>  taskOptional =taskRepository.findByActivoTrueAndId(idTask);
        Optional<Task>  taskOptional =taskRepository.findByActivoTrueAndCodigoTarea(codigoTarea);
        if (taskOptional.isPresent())
        {
            Task task = taskOptional.get();
            if (task.getEstadoTarea().equalsIgnoreCase(EstadoTarea.DONE.getLabel()))
            {
                return    genericResponse
                        .code(Status.BAD_REQUEST.getStatusCode())
                        .success(false)
                        .message("No se puede asignar una task finalizada (DONE).");
            }

            // Comunicación asíncrona: validar si el usuario existe
            boolean usuarioExiste = validarUsuario(idUsuario);
            if (!usuarioExiste)
            {
                return    genericResponse
                        .code(Status.NOT_FOUND.getStatusCode())
                        .success(false)
                        .message("El usuario con ID " + idUsuario + " no existe.");
            }
            task.setUsuarioIdAsignado(idUsuario);
            Task taskAsignada = taskRepository.save(task);

            //(puede ser un log en el sistema)
            log.info("TAREA ASIGNADA -> Task '{}' fue asignada al usuario {}", taskAsignada.getCodigoTarea(), idUsuario);
            return    genericResponse
                    .code(Status.OK.getStatusCode())
                    .success(true)
                    .message("TAREA ASIGNADA -> Task "+taskAsignada.getCodigoTarea()+" fue asignada al usuario "+idUsuario);


        }else
        {
            
            return    genericResponse
                    .code(Status.NOT_FOUND.getStatusCode())
                    .success(false)
                    .message("la Tarea " + codigoTarea + " no existe.");
        }

    }



    @Override
    public GenericResponse tareasPorUsuario(long idUsuario)
    {
        GenericResponse genericResponse  =new GenericResponse();
        taskRepository.findByUsuarioIdAsignado(idUsuario).ifPresentOrElse(taskList ->
        {
            List<TaskDto> taskDtoList = taskMapper.toTaskDtoList(taskList);
            genericResponse.data(taskDtoList)
                    .success(true)
                    .code(Status.OK.getStatusCode())
                    .message("Consulta Existosa");

        }, () -> {

            throw new GenericExceptionUtils("No se encontraron Datos", Status.OK);
        });
        return genericResponse;
    }


    // Método para comunicarse con client-service por RabbitMQ

    @Override
    public boolean validarUsuario(long id)
    {
        try {
            return Boolean.TRUE.equals(
                    rabbitTemplate.convertSendAndReceive( "validar-usuario-queue", id)
            );
        } catch (Exception e) {
            log.error("Error comunicando con client-service para validar usuario", e);
            return false;
        }
    }





}