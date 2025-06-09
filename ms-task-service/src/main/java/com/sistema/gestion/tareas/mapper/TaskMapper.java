package com.sistema.gestion.tareas.mapper;

import com.sistema.gestion.tareas.dto.TaskDto;
import com.sistema.gestion.tareas.model.Task;
import com.sistema.gestion.tareas.util.EstadoTarea;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper
{
    Task taskDtoToTask(TaskDto taskDto);

    TaskDto taskToTaskDto(Task task);

    List<TaskDto> toTaskDtoList(List<Task> tipoPescaProductoList);

    List<Task> toTaskList(List<TaskDto> tipoPescaProductoDtoList);


    // Estos métodos son útiles si trabajas con otros niveles intermedios
    default String mapEstadoTareaToString(EstadoTarea estado) {
        return estado != null ? estado.getLabel() : null;
    }

    default EstadoTarea mapStringToEstadoTarea(String label) {
        return EstadoTarea.fromLabel(label);
    }
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Task updateTaskFromTaskDto(TaskDto taskDto, @MappingTarget Task task);

}
