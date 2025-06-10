package com.sistema.gestion.tareas.controller;

import com.sistema.gestion.tareas.dto.UserDto;
import com.sistema.gestion.tareas.service.UserService;
import com.sistema.gestion.tareas.util.GenericResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private  UserService userServices;


    @Operation(summary = "Listar todos los usuarios activos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios encontrados"),
            @ApiResponse(responseCode = "404", description = "No se encontraron usuarios")
    })
    @GetMapping("findAllUser/")
    public ResponseEntity<GenericResponse>  findAllUser()
    {
        return userServices.findAllUser();
    }

    @Operation(summary = "Buscar usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("findByIdUser/")
    public ResponseEntity<GenericResponse> findByIdUser(@RequestParam Long id)
    {
        return userServices.findByIdUser(id);
    }

    @Operation(summary = "Guardar un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado"),
            @ApiResponse(responseCode = "400", description = "Validación fallida"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PostMapping("saveUser/")
    public ResponseEntity<GenericResponse> saveUpdateUser(@RequestBody UserDto userS)
    {
        return userServices.saveUpdateUser(userS);
    }

    @Operation(summary = "Actualizar un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario modificado"),
            @ApiResponse(responseCode = "201", description = "Usuario creado"),
            @ApiResponse(responseCode = "400", description = "Validación fallida"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PutMapping("updateUser/")
    public ResponseEntity<GenericResponse> updateUser(@RequestBody UserDto userS)
    {
        return userServices.saveUpdateUser(userS);
    }

    @Operation(summary = "Eliminar (desactivar) usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @DeleteMapping("deleteUser/")
    public ResponseEntity<GenericResponse> deleteUser(@RequestParam long id)
    {
        return userServices.deleteUser(id);
    }


  
    
    
}