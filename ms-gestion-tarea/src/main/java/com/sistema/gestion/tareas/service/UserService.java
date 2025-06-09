package com.sistema.gestion.tareas.service;

import com.sistema.gestion.tareas.dto.UserDto;
import com.sistema.gestion.tareas.mapper.UserMapper;
import com.sistema.gestion.tareas.model.User;
import com.sistema.gestion.tareas.repository.UserRepository;
import com.sistema.gestion.tareas.util.GenericExceptionUtils;
import com.sistema.gestion.tareas.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    


    public GenericResponse findAllUser()
    {
        GenericResponse genericResponse  =new GenericResponse();
        userRepository.findAllByEstadoTrue().ifPresentOrElse(userList ->
        {
            List<UserDto> userDtoList = userMapper.toUserDtoList(userList);
            genericResponse.data(userDtoList)
                    .success(true)
                    .code(200)
                    .message("Consulta Existosa");

        }, () -> {

            throw new GenericExceptionUtils("No se encontraron Datos", Status.OK);
        });
        return genericResponse;
    }



    public GenericResponse findByIdUser(long id)
    {
        GenericResponse genericResponse  =new GenericResponse();
        userRepository.findByEstadoTrueAndId(id).ifPresentOrElse(user ->
        {
            UserDto userDto = userMapper.userToUserDto(user);
            genericResponse.data(userDto)
                    .success(true)
                    .code(200)
                    .message("Consulta Existosa");

        }, () -> {
            genericResponse.success(false)
                    .code(404)
                    .message("No se encontraron Datos");
        });

        return genericResponse;
    }

    @Transactional(rollbackFor = {RuntimeException.class} )
    public GenericResponse saveUpdateUser(UserDto userS)
    {
        GenericResponse genericResponse  =new GenericResponse();

        userRepository.
                findByEstadoTrueAndId(Objects.requireNonNullElse(userS.getId(), 0L))
                .ifPresentOrElse(user ->
                {
                    User userMap = userMapper.userDtoToUser(userS);
                    userMap.setId(user.getId());
                    userMap.setFechaModifica(Instant.now());
                    userMap.setFechaCreacion(user.getFechaCreacion());
                    userMap.setUsuarioCreacion(user.getUsuarioCreacion());
                    userMap.setEstado(user.getEstado());
                    genericResponse.data(userMapper.userToUserDto(userRepository.save(userMap)))
                            .success(true)
                            .code(200)
                            .message("Modificación Existosa");

                }, () ->
                {
                    Optional<User> userOptional =userRepository.findByEstadoTrueAndIdentificacion(Objects.requireNonNullElse(userS.getIdentificacion(), ""));

                    if (userOptional.isPresent())
                    {
                        genericResponse
                                .success(false)
                                .code(400)
                                .message("EL número de Identificacion : "+userOptional.get().getIdentificacion() +" ya se encuentra registrada"  );
                    }
                    else
                    {
                        userS.setEstado(true);
                        User userSave= userRepository.save(userMapper.userDtoToUser(userS));
                        genericResponse.data(userMapper.userToUserDto(userSave))
                                .success(true)
                                .code(201)
                                .message("Guardado Existosa");
                    }

                });

        return genericResponse;
    }


    public GenericResponse deleteUser(Long id)
    {
        GenericResponse genericResponse  =new GenericResponse();

        userRepository.findByEstadoTrueAndId(id).ifPresentOrElse(user ->
        {
            user.setFechaModifica(Instant.now());
            user.setEstado(false);
            User userSave= userRepository.save(user);
            genericResponse.data(userMapper.userToUserDto(userSave))
                    .success(true)
                    .code(200)
                    .message("Elimino Existosamente");

        }, () ->
        {
            genericResponse.success(false)
                    .code(404)
                    .message("No se Elimino");
        });

        return genericResponse;
    }
    public boolean existeUsuarioPorId(long id)
    {
        boolean existsById =false;
        if (userRepository.findById(id).isPresent())
        {
            existsById =true;
        }else
        {
            existsById =false;
        }

        return existsById;
    }

}