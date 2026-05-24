package com.myCompany.controllers;

import com.myCompany.models.dtos.ClientCreateRequest;
import com.myCompany.models.dtos.ClientUpdateRequest;
import com.myCompany.services.ClientService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.Optional;
import java.util.UUID;

@Tag(name = "Clients", description = "Endpoints for managing clients")
@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
public class ClientsController {
    private final ClientService service;

    @Inject
    public ClientsController(ClientService service) {
        this.service = service;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(ClientCreateRequest model) {
        var res = service.createClient(model);
        return Response.status(res.getStatusCode()).entity(res).build();
    }

    @GET
    public Response getAll(
            @QueryParam("country") Optional<String> country,
            @QueryParam("page") Optional<Integer> page,
            @QueryParam("size") Optional<Integer> size) {

        var res = service.getClients(country, page, size);
        return Response.status(res.getStatusCode()).entity(res).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") UUID id) {
        var res = service.getClientById(id);
        return Response.status(res.getStatusCode()).entity(res).build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") UUID id, ClientUpdateRequest model) {
        var res = service.updateClient(id, model);
        return Response.status(res.getStatusCode()).entity(res).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        var res = service.deleteClient(id);
        return Response.status(res.getStatusCode()).entity(res).build();
    }
}
