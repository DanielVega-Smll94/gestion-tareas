package com.sistema.gestion.tareas.util;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

import java.net.URI;

public class GenericExceptionUtils extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;
    private String message;
    private Status status;
    private boolean success;
    private Object data;

    public GenericExceptionUtils(Exception ex) {
        super(null, ex.getMessage(), Status.BAD_REQUEST);
        this.message = null;
        this.status = Status.BAD_REQUEST;
        this.data = null;
    }

    public GenericExceptionUtils(String message, Status status) {
        super(null, message, status);
        this.message = message;
        this.status = status;
        this.data = null;
    }

    public GenericExceptionUtils(String message, Status status, boolean success) {
        super(null, message, status);
        this.message = message;
        this.status = status;
        this.success = success;
        this.data = null;
    }

    public GenericExceptionUtils(URI type, String title, StatusType status) {
        super(type, title, status);
    }

    public GenericExceptionUtils(String message, Status status, boolean success, Object data) {
        super(null, message, status);
        this.message = message;
        this.status = status;
        this.success = success;
        this.data = data;
    }

    public GenericExceptionUtils(Status status, boolean success, Object data) {
        super(null, null, status);
        this.message = null;
        this.status = status;
        this.success = success;
        this.data = data;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
