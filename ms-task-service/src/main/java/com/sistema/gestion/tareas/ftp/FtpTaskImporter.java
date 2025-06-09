package com.sistema.gestion.tareas.ftp;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.sistema.gestion.tareas.dto.TaskDto;
import com.sistema.gestion.tareas.mapper.TaskMapper;
import com.sistema.gestion.tareas.model.Task;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class FtpTaskImporter {

    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final ObjectMapper objectMapper;

    private final TaskMapper taskMapper;

    private final String sftpHost = "";
    private final int sftpPort = 22;
    private final String sftpUser = "root";
    private final String sftpPassword = "";
    private final String sftpFilePath = "/usr/local/gestion-tareas/pendientes/tareas.json";

    // Ejecutar cada 5 minutos 300000
    @Scheduled(fixedRate = 300)
    public void importarTareasDesdeSFTP() {
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(sftpUser, sftpHost, sftpPort);
            session.setPassword(sftpPassword);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            try (InputStream input = channelSftp.get(sftpFilePath))
            {
                List<TaskDto> tareas = objectMapper.readValue(input, new TypeReference<>() {});
                //tareas.forEach(taskRepository::save);
                tareas.parallelStream()
                        .forEach(xcv->{
                            GenericResponse genericResponse = taskService.saveUpdateTask(xcv);
                            if (genericResponse.getSuccess())
                            {
                                log.info("Importadas {} tareas desde SFTP", tareas.size());
                            }else
                            {
                                System.out.println("Tareas desde SFTP"+genericResponse.getMessage());
                                log.info("Tareas desde SFTP",genericResponse);
                            }

                        });

            }

            // Opcional: eliminar el archivo después de procesarlo
           // channelSftp.rm(sftpFilePath);

        } catch (Exception e) {
            log.error("Error leyendo tareas desde SFTP", e);
        } finally {
            if (channelSftp != null && channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }
}