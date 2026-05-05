package com.github.rogerioja89.resource;

import com.github.rogerioja89.dto.SimulacaoHistoricoDTO;
import com.github.rogerioja89.dto.SimulacaoRequestDTO;
import com.github.rogerioja89.dto.SimulacaoResponseDTO;
import com.github.rogerioja89.service.SimulacaoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

// Resource é o "controller" da API REST — define as rotas HTTP e delega ao Service.
// @Path define a URL base: /simulacoes
// @Produces e @Consumes informam que trabalhamos com JSON.
@Path("/simulacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SimulacaoResource {

    @Inject
    SimulacaoService simulacaoService;

    // POST /simulacoes
    // @Valid aciona o Hibernate Validator nas anotações do SimulacaoRequestDTO.
    // Se algum campo falhar a validação, o Quarkus retorna 400 automaticamente.
    @POST
    public Response criar(@Valid SimulacaoRequestDTO request) {
        SimulacaoResponseDTO resposta = simulacaoService.simular(request);
        return Response.status(Response.Status.CREATED).entity(resposta).build();
    }

    // GET /simulacoes?clienteId=123
    // @QueryParam captura o parâmetro da query string da URL.
    @GET
    public List<SimulacaoHistoricoDTO> historico(@QueryParam("clienteId") Long clienteId) {
        if (clienteId == null) {
            throw new WebApplicationException(
                Response.status(Response.Status.BAD_REQUEST)
                        .type(MediaType.APPLICATION_JSON)
                        .entity("{\"erro\": \"O parâmetro clienteId é obrigatório\"}")
                        .build()
            );
        }
        return simulacaoService.buscarHistorico(clienteId);
    }
}