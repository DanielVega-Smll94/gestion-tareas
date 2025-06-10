package com.sistema.gestion.tareas.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "sftp")
public class SftpProperties {
    private String host;
    private int port;
    private String user;
    private String password;
    private String filePath;
}