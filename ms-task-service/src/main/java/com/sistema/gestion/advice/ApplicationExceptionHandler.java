package com.sistema.gestion.advice;

import com.sistema.gestion.tareas.util.GenericExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler({ GenericExceptionUtils.class })
    public ResponseEntity<Map<String, Object>> genericExceptionUtils(GenericExceptionUtils ex) {

        HashMap<String, Object> response = new HashMap<>();
        response.put("success", ex.getSuccess());

        if (Objects.nonNull(ex.getMessage())) {
            response.put("message", ex.getMessage());
        }

        if (Objects.nonNull(ex.getData())) {
            response.put("data", ex.getData());
        }

        return ResponseEntity.status(ex.getStatus().getStatusCode()).body(response);
    }
}
