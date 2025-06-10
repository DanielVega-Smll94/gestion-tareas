package com.sistema.gestion.tareas.service;

import com.sistema.gestion.tareas.dto.UserDto;
import com.sistema.gestion.tareas.mapper.UserMapper;
import com.sistema.gestion.tareas.model.User;
import com.sistema.gestion.tareas.repository.UserRepository;
import com.sistema.gestion.tareas.util.GenericExceptionUtils;
import com.sistema.gestion.tareas.util.GenericResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Status;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository ,UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    


    public ResponseEntity<GenericResponse> findAllUser()
    {
        GenericResponse genericResponse  =new GenericResponse();
        List<User> userDtoList = userRepository.findAllByEstadoTrue();
        if(userDtoList.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                            genericResponse
                                    .success(false)
                                    .code(Status.NOT_FOUND.getStatusCode())
                                    .message("No se encontraron Datos")
                    );

        return ResponseEntity
                .ok()
                .body(genericResponse.data(userMapper.toUserDtoList(userDtoList))
                        .success(true)
                        .code(Status.OK.getStatusCode())
                        .message("Consulta Existosa")
                ) ;
    }



    public ResponseEntity<GenericResponse> findByIdUser(long id)
    {
        GenericResponse genericResponse  = new GenericResponse();
        userRepository.findByEstadoTrueAndId(id).ifPresentOrElse(user ->
        {
            UserDto userDto = userMapper.userToUserDto(user);
            genericResponse.data(userDto)
                    .success(true)
                    .code(Status.OK.getStatusCode())
                    .message("Consulta Existosa");

        }, () -> {
            genericResponse.success(false)
                    .code(Status.NOT_FOUND.getStatusCode())
                    .message("No se encontraron Datos");
        });

        if (genericResponse.getSuccess())
            return ResponseEntity.ok()
                    .body(genericResponse);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);

    }

    @Transactional(rollbackFor = {RuntimeException.class} )
    public ResponseEntity<GenericResponse> saveUpdateUser(UserDto userS)
    {
        GenericResponse genericResponse  = new GenericResponse();
        try {

            Optional<User> opUser = userRepository.
                    findByEstadoTrueAndId(Objects.requireNonNullElse(userS.getId(), 0L));
            if (opUser.isPresent()) {
                User userMap = userMapper.userDtoToUser(userS);
                userMap.setId(opUser.get().getId());
                userMap.setFechaModifica(Instant.now());
                userMap.setFechaCreacion(opUser.get().getFechaCreacion());
                userMap.setUsuarioCreacion(opUser.get().getUsuarioCreacion());
                userMap.setEstado(opUser.get().getEstado());
                return ResponseEntity.ok()
                        .body(
                                genericResponse.data(userMapper.userToUserDto(userRepository.save(userMap)))
                                        .success(true)
                                        .code(Status.OK.getStatusCode())
                                        .message("Modificación Existosa")
                        );
            }else{
                //Optional<User> userOptional =userRepository.findByEstadoTrueAndIdentificacion(Objects.requireNonNullElse(userS.getIdentificacion(), ""));
                Optional<User> userOptional = userRepository.findByIdentificacion(Objects.requireNonNullElse(userS.getIdentificacion(), ""));

                if (userOptional.isPresent()) {
                    Long inputId = userS.getId();
                    User existing = userOptional.get();
                    if (inputId == null || !Objects.equals(existing.getId(), userS.getId())) {
                        String mensaje = existing.getEstado()
                                ? "El número de Identificación: " + existing.getIdentificacion() + " ya se encuentra registrado"
                                : "El número de Identificación: " + existing.getIdentificacion() + " ya se encuentra registrado pero está inactivo";

                        return ResponseEntity.badRequest()
                                .body(
                                        genericResponse
                                                .success(false)
                                                .code(Status.BAD_REQUEST.getStatusCode())
                                                .message(mensaje)
                                );
                    }
                }

                userS.setEstado(true);
                User userSave = userRepository.save(userMapper.userDtoToUser(userS));
                return ResponseEntity.ok()
                        .body(
                                genericResponse.data(userMapper.userToUserDto(userSave))
                                        .success(true)
                                        .code(Status.CREATED.getStatusCode())
                                        .message("Guardado exitoso")
                        );
            }
        }catch (Exception e){
            return ResponseEntity.internalServerError()
                    .body(
                            genericResponse
                                    .success(false)
                                    .code(Status.INTERNAL_SERVER_ERROR.getStatusCode())
                                    .message(
                                            "MESSAGE: "+StringUtils.defaultString(e.getMessage()) +
                                                    "CAUSE: "+Objects.toString(e.getCause(), StringUtils.EMPTY)
                                    )
                    );
        }
    }


    public ResponseEntity<GenericResponse>  deleteUser(Long id)
    {
        GenericResponse genericResponse  =new GenericResponse();

        Optional<User> optionalUser = userRepository.findByEstadoTrueAndId(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFechaModifica(Instant.now());
            user.setEstado(false);
            User userSave= userRepository.save(user);

            return ResponseEntity.ok()
                    .body(
                            genericResponse.data(userMapper.userToUserDto(userSave))
                                    .success(true)
                                    .code(Status.OK.getStatusCode())
                                    .message("Eliminado Exitosamente")
                    );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(genericResponse.success(false)
                            .code(Status.NOT_FOUND.getStatusCode())
                            .message("No se Elimino"));
        }
    }
    public boolean existeUsuarioPorId(long id)
    {
        return userRepository.findById(id).isPresent();
    }

}