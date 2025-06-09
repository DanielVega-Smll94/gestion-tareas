package com.sistema.gestion.tareas.repository;

import com.sistema.gestion.tareas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<User, Long>
{

    Optional<List<User>> findAllByEstadoTrue();

    Optional<User> findByEstadoTrueAndId(long idUser);


    Optional<User> findByEstadoTrueAndIdentificacion(String identificacion );
}