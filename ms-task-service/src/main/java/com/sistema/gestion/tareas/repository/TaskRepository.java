package com.sistema.gestion.tareas.repository;

import com.sistema.gestion.tareas.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>
{
    Optional<Task> findByCodigoTarea(String codigo);

    Optional<List<Task>> findAllByActivoTrue();




    Optional<List<Task>> findByUsuarioIdAsignado(Long usuarioId);

    Optional<Task> findByActivoTrueAndId(long idUser);


    Optional<Task> findByActivoTrueAndCodigoTarea(String codigoTarea );

}