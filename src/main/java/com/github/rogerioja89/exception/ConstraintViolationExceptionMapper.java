package com.github.rogerioja89.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        String mensagens = e.getConstraintViolations().stream()
            .map(v -> extrairNomeCampo(v) + ": " + v.getMessage())
            .collect(Collectors.joining("; "));
        return Response.status(Response.Status.BAD_REQUEST)
            .type(MediaType.APPLICATION_JSON)
            .entity(new ErroResponse(mensagens))
            .build();
    }

    // O path da violation vem no formato "metodo.parametro.campo" — extrai só o último segmento.
    private String extrairNomeCampo(ConstraintViolation<?> violation) {
        String path = violation.getPropertyPath().toString();
        return path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
    }
}
