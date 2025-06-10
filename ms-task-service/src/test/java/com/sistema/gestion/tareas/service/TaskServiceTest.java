package com.sistema.gestion.tareas.service;

import com.sistema.gestion.tareas.dto.TaskDto;
import com.sistema.gestion.tareas.model.Task;
import com.sistema.gestion.tareas.repository.TaskRepository;
import com.sistema.gestion.tareas.util.EstadoTarea;
import com.sistema.gestion.tareas.util.GenericResponse;
import org.testng.annotations.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.sistema.gestion.tareas.mapper.TaskMapper;
import org.zalando.problem.Status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void testFindAllTasks() {
        Task task1 = new Task(1L, "Descripción 1");
        Task task2 = new Task(2L, "Descripción 2");

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<Task> result = taskRepository.findAll();

        assertEquals(772, result.size());
        assertEquals("Tarea 1", result.get(0).getCodigoTarea());
    }

    @Test
    void testFindByIdTask_found() {
        Task task = new Task();
        task.setId(2L);
        when(taskRepository.findByActivoTrueAndId(2L)).thenReturn(Optional.of(task));

        TaskDto dto = new TaskDto();
        when(taskMapper.taskToTaskDto(task)).thenReturn(dto);

        GenericResponse response = taskService.findByIdTask(2L);

        assertTrue(response.getSuccess());
        assertEquals(Status.OK.getStatusCode(), response.getCode());
        assertEquals(dto, response.getData());
    }

    @Test
    void testSaveUpdateTask_createNew() {
        TaskDto inputDto = new TaskDto();
        inputDto.setCodigoTarea("code1");

        when(taskRepository.findByActivoTrueAndId(0L)).thenReturn(Optional.empty());
        when(taskRepository.findByActivoTrueAndCodigoTarea("code1")).thenReturn(Optional.empty());

        Task entity = new Task();
        when(taskMapper.taskDtoToTask(inputDto)).thenReturn(entity);
        Task savedEntity = new Task();
        savedEntity.setId(3L);
        when(taskRepository.save(entity)).thenReturn(savedEntity);

        TaskDto outputDto = new TaskDto();
        when(taskMapper.taskToTaskDto(savedEntity)).thenReturn(outputDto);

        GenericResponse response = taskService.saveUpdateTask(inputDto);

        assertTrue(response.getSuccess());
        assertEquals(Status.CREATED.getStatusCode(), response.getCode());
        assertEquals(outputDto, response.getData());
    }

    @Test
    void testDeleteTask_success() {
        Task task = new Task();
        task.setId(4L);
        when(taskRepository.findByActivoTrueAndId(4L)).thenReturn(Optional.of(task));
        Task saved = new Task();
        saved.setId(4L);
        when(taskRepository.save(task)).thenReturn(saved);
        TaskDto dto = new TaskDto();
        when(taskMapper.taskToTaskDto(saved)).thenReturn(dto);

        GenericResponse response = taskService.deleteTask(4L);

        assertTrue(response.getSuccess());
        assertEquals(Status.OK.getStatusCode(), response.getCode());
        assertEquals(dto, response.getData());
    }

    @Test
    void testActualizarEstado_success() {
        Task task = new Task();
        task.setId(5L);
        when(taskRepository.findByActivoTrueAndId(5L)).thenReturn(Optional.of(task));
        Task saved = new Task();
        saved.setId(5L);
        when(taskRepository.save(task)).thenReturn(saved);
        TaskDto dto = new TaskDto();
        when(taskMapper.taskToTaskDto(saved)).thenReturn(dto);

        GenericResponse response = taskService.actualizarEstado(5L, EstadoTarea.DOING.getLabel());

        assertTrue(response.getSuccess());
        assertEquals(Status.OK.getStatusCode(), response.getCode());
        assertEquals(dto, response.getData());
    }

    @Test
    void testAsignarTask_success() {
        Task task = new Task();
        task.setCodigoTarea("code2");
        task.setEstadoTarea(EstadoTarea.BACKLOG.getLabel());
        when(taskRepository.findByActivoTrueAndCodigoTarea("code2")).thenReturn(Optional.of(task));
        when(rabbitTemplate.convertSendAndReceive(anyString(), eq(10L))).thenReturn(Boolean.TRUE);
        Task saved = new Task();
        saved.setCodigoTarea("code2");
        when(taskRepository.save(task)).thenReturn(saved);

        GenericResponse response = taskService.asignarTask("code2", 10L);

        assertTrue(response.getSuccess());
        assertEquals(Status.OK.getStatusCode(), response.getCode());
    }

    @Test
    void testTareasPorUsuario_success() {
        Task task = new Task();
        List<Task> tasks = Collections.singletonList(task);
        when(taskRepository.findByUsuarioIdAsignado(7L)).thenReturn(Optional.of(tasks));
        TaskDto dto = new TaskDto();
        when(taskMapper.toTaskDtoList(tasks)).thenReturn(Collections.singletonList(dto));

        GenericResponse response = taskService.tareasPorUsuario(7L);

        assertTrue(response.getSuccess());
        assertEquals(Status.OK.getStatusCode(), response.getCode());
        assertEquals(Collections.singletonList(dto), response.getData());
    }

    @Test
    void testValidarUsuario_success() {
        when(rabbitTemplate.convertSendAndReceive("validar-usuario-queue", 8L)).thenReturn(Boolean.TRUE);
        assertTrue(taskService.validarUsuario(8L));
    }
}
