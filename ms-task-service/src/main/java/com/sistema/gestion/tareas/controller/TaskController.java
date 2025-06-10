package com.sistema.gestion.tareas.controller;


import com.sistema.gestion.tareas.dto.TaskDto;
import com.sistema.gestion.tareas.service.TaskService;
import com.sistema.gestion.tareas.util.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "TaskMSv1", description = "API REST para administración de Task")
@RestController
@RequestMapping("/api/task/")
@CrossOrigin(origins = "*")
public class TaskController
{


    @Autowired
    TaskService taskService;

    @Operation(summary = "Listar todas las tareas activas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tareas encontrados"),
            @ApiResponse(responseCode = "404", description = "No se encontraron Tarea")
    })
    @GetMapping("findAllTask/")
    public GenericResponse findAllTask()
    {
        return taskService.findAllTask();
    }

    @Operation(summary = "Buscar tarea por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    @GetMapping("findByIdTask/")
    public GenericResponse findByIdTask(@RequestParam Long id)
    {
        return taskService.findByIdTask(id);
    }

    @Operation(summary = "Guardar una nueva tarea")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarea creada"),
            @ApiResponse(responseCode = "400", description = "Validación fallida"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PostMapping("saveTask/")
    public GenericResponse saveUpdateTask(@RequestBody TaskDto taskS)
    {
        return taskService.saveUpdateTask(taskS);
    }

    @Operation(summary = "Actualizar una nueva tarea")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea modificada"),
            @ApiResponse(responseCode = "201", description = "Tarea creada"),
            @ApiResponse(responseCode = "400", description = "Validación fallida"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PutMapping("updateTask/")
    public GenericResponse updateTask(@RequestBody TaskDto taskS)
    {
        return taskService.saveUpdateTask(taskS);
    }

    @Operation(summary = "Eliminar (desactivar) tarea por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea eliminado"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    @DeleteMapping("deleteTask/")
    public GenericResponse deleteTask(@RequestParam long id)
    {
        return taskService.deleteTask(id);
    }

    @Operation(summary = "Actualizar estado de una tarea")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Estado inválido"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    @PutMapping("actualizarEstado/")
    public GenericResponse actualizarEstado(@RequestParam  Long id, @RequestParam  String nuevoEstado)
    {
        return taskService.actualizarEstado( id,  nuevoEstado);
    }

    @Operation(summary = "Asignar una tarea a un usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarea asignada exitosamente"),
            @ApiResponse(responseCode = "400", description = "No se puede asignar tarea finalizada"),
            @ApiResponse(responseCode = "404", description = "Tarea o usuario no encontrado")
    })
    @PutMapping("asignarTask/")
    public GenericResponse asignarTask(@RequestParam String codigoTarea, long idUsuario )
    {
        return taskService.asignarTask( codigoTarea,  idUsuario);
    }


    @Operation(summary = "Obtener tareas por usuario asignado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta exitosa de tareas"),
            @ApiResponse(responseCode = "404", description = "No se encontraron tareas para el usuario")
    })
    @GetMapping("tareasPorUsuario/")
    public GenericResponse tareasPorUsuario(@RequestParam Long idUsuario)
    {
        return taskService.tareasPorUsuario(idUsuario);
    }


    @Operation(summary = "Validar existencia de un usuario por medio de RabbitMQ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Devuelve true si el usuario existe, false en caso contrario")
    })
    @GetMapping("validarUsuario/")
    public Boolean validarUsuario(@RequestParam Long id)
    {
        return taskService.validarUsuario(id);
    }


}