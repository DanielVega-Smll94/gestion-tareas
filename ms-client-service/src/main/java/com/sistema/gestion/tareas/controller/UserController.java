package com.sistema.gestion.tareas.controller;

import com.sistema.gestion.tareas.dto.UserDto;
import com.sistema.gestion.tareas.service.UserService;
import com.sistema.gestion.tareas.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private  UserService userServices;


    @GetMapping("findAllUser/")
    public GenericResponse findAllUser()
    {
        return userServices.findAllUser();
    }

    @GetMapping("findByIdUser/")
    public GenericResponse findByIdUser(@RequestParam Long id)
    {
        return userServices.findByIdUser(id);
    }


    @PostMapping("saveUser/")
    public GenericResponse saveUpdateUser(@RequestBody UserDto userS)
    {
        return userServices.saveUpdateUser(userS);
    }

    @PutMapping("updateUser/")
    public GenericResponse updateUser(@RequestBody UserDto userS)
    {
        return userServices.saveUpdateUser(userS);
    }



    @DeleteMapping("deleteUser/")
    public GenericResponse deleteUser(@RequestParam long id)
    {
        return userServices.deleteUser(id);
    }


  
    
    
}