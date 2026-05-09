package com.github.rogerioja89.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NegocioExceptionMapper implements ExceptionMapper<NegocioException> {

    @Override
    public Response toResponse(NegocioException e) {
        return Response.status(e.getStatus())
            .type(MediaType.APPLICATION_JSON)
            .entity(new ErroResponse(e.getMessage()))
            .build();
    }
}
