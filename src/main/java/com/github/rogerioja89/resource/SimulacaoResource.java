package com.github.rogerioja89.resource;

import com.github.rogerioja89.dto.SimulacaoHistoricoDTO;
import com.github.rogerioja89.dto.SimulacaoRequestDTO;
import com.github.rogerioja89.dto.SimulacaoResponseDTO;
import com.github.rogerioja89.exception.NegocioException;
import com.github.rogerioja89.service.SimulacaoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/simulacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SimulacaoResource {

    @Inject
    SimulacaoService simulacaoService;

    // @Valid aciona o Hibernate Validator; Quarkus retorna 400 automaticamente se a validação falhar.
    @POST
    public Response criar(@Valid SimulacaoRequestDTO request) {
        SimulacaoResponseDTO resposta = simulacaoService.simular(request);
        return Response.status(Response.Status.CREATED).entity(resposta).build();
    }

    @GET
    public List<SimulacaoHistoricoDTO> historico(@QueryParam("clienteId") Long clienteId) {
        // @Valid não cobre @QueryParam — Quarkus só valida automaticamente o corpo da requisição.
        if (clienteId == null) {
            throw new NegocioException(400, "O parâmetro clienteId é obrigatório");
        }
        return simulacaoService.buscarHistorico(clienteId);
    }
}