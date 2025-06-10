package com.sistema.gestion.tareas.messaging;


import com.sistema.gestion.tareas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.zalando.problem.Status;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final UserService userService;

    @RabbitListener(queues = "${queue.validar-usuario}")
    public boolean validarUsuario(long id)
    {
        try
        {

            return userService.existeUsuarioPorId(id);
        }    catch (Exception e)
        {
      System.out.println("Exception"+e.getMessage());
    }
        return false;
    }
}