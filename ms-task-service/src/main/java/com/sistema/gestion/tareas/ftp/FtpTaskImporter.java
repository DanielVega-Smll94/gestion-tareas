package com.sistema.gestion.tareas.ftp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.sistema.gestion.tareas.dto.TaskDto;
import com.sistema.gestion.tareas.mapper.TaskMapper;
import com.sistema.gestion.tareas.repository.TaskRepository;
import com.sistema.gestion.tareas.service.TaskService;
import com.sistema.gestion.tareas.util.GenericResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import com.sistema.gestion.tareas.config.SftpProperties;

@RequiredArgsConstructor
@Slf4j
@Service
public class FtpTaskImporter {

    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final ObjectMapper objectMapper;
    private final TaskMapper taskMapper;
    private final SftpProperties sftpProperties;

    @Scheduled(fixedRate = 300000)
    public void importarTareasDesdeSFTP() {
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(sftpProperties.getUser(), sftpProperties.getHost(), sftpProperties.getPort());
            session.setPassword(sftpProperties.getPassword());

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            try (InputStream input = channelSftp.get(sftpProperties.getFilePath())) {
                List<TaskDto> tareas = objectMapper.readValue(input, new TypeReference<>() {});
                tareas.parallelStream().forEach(taskDto -> {
                    GenericResponse response = taskService.saveUpdateTask(taskDto);
                    if (response.getSuccess()) {
                        log.info("Importadas {} tareas desde SFTP", tareas.size());
                    } else {
                        log.warn("Tareas desde SFTP: {}", response.getMessage());
                    }
                });
            }

        } catch (Exception e) {
            log.error("Error leyendo tareas desde SFTP", e);
        } finally {
            if (channelSftp != null && channelSftp.isConnected()) channelSftp.disconnect();
            if (session != null && session.isConnected()) session.disconnect();
        }
    }
}