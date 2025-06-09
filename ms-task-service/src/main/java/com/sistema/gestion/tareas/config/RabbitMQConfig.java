package com.sistema.gestion.tareas.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nombre de la cola; debe coincidir con la usada en client-service
    private static final String VALIDAR_USUARIO_QUEUE = "validar-usuario-queue";

    @Bean
    public Queue queueValidarUsuario() {
        return new Queue(VALIDAR_USUARIO_QUEUE, false); // no durable, no exclusive, no auto-delete
    }

    // Conversor de mensajes JSON
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate con el conversor de mensajes
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        template.setReplyTimeout(5000); // opcional: 5 segundos de espera por respuesta
        return template;
    }
}