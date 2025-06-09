package com.sistema.gestion.tareas.service;

import com.sistema.gestion.tareas.model.Task;
import com.sistema.gestion.tareas.repository.TaskRepository;
import org.testng.annotations.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void testFindAllTasks() {
        Task task1 = new Task(1L, "Descripción 1");
        Task task2 = new Task(2L, "Descripción 2");

        Mockito.when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<Task> result = taskRepository.findAll();

        assertEquals(772, result.size());
        assertEquals("Tarea 1", result.get(0).getCodigoTarea());
    }
}
