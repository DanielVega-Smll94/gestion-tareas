package com.sistema.gestion.tareas.controller;


import com.sistema.gestion.tareas.dto.TaskDto;
import com.sistema.gestion.tareas.service.TaskService;
import com.sistema.gestion.tareas.util.GenericResponse;
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

    @GetMapping("findAllTask/")
    public GenericResponse findAllTask()
    {
        return taskService.findAllTask();
    }


    @GetMapping("findByIdTask/")
    public GenericResponse findByIdTask(@RequestParam Long id)
    {
        return taskService.findByIdTask(id);
    }


    @PostMapping("saveTask/")
    public GenericResponse saveUpdateTask(@RequestBody TaskDto taskS)
    {
        return taskService.saveUpdateTask(taskS);
    }

    @PutMapping("updateTask/")
    public GenericResponse updateTask(@RequestBody TaskDto taskS)
    {
        return taskService.saveUpdateTask(taskS);
    }



    @DeleteMapping("deleteTask/")
    public GenericResponse deleteTask(@RequestParam long id)
    {
        return taskService.deleteTask(id);
    }

    @PutMapping("actualizarEstado/")
    public GenericResponse actualizarEstado(@RequestParam  Long id, @RequestParam  String nuevoEstado)
    {
        return taskService.actualizarEstado( id,  nuevoEstado);
    }

    @PutMapping("asignarTask/")
    public GenericResponse asignarTask(@RequestParam String codigoTarea, long idUsuario )
    {
        return taskService.asignarTask( codigoTarea,  idUsuario);
    }


    @GetMapping("tareasPorUsuario/")
    public GenericResponse tareasPorUsuario(@RequestParam Long idUsuario)
    {
        return taskService.tareasPorUsuario(idUsuario);
    }


    @GetMapping("validarUsuario/")
    public Boolean validarUsuario(@RequestParam Long id)
    {
        return taskService.validarUsuario(id);
    }


}