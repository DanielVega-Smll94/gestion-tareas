package com.sistema.gestion.tareas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MsTaskServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsTaskServiceApplication.class, args);
    }

}
