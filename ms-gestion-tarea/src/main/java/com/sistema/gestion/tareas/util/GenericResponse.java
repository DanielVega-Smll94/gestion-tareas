package com.sistema.gestion.tareas.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.ToString;

@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericResponse {

    private Integer code;
    private Object data;
    private String message;
    private Boolean success;

    public GenericResponse data(Object data){
        this.data = data;
        return this;
    }

    public GenericResponse message(String message){
        this.message = message;
        return this;
    }

    public GenericResponse success(Boolean success){
        this.success = success;
        return this;
    }

    public GenericResponse code(Integer code){
        this.code = code;
        return this;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
