package com.myCompany.controllers;

import com.myCompany.services.ExternalService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Optional;


@Tag(name = "Countries", description = "Endpoints for consulting Rest Countries API")
@Path("/countries")
@Produces(MediaType.APPLICATION_JSON)
public class RestCountriesController {
    private final ExternalService service;

    @Inject
    public RestCountriesController(ExternalService service) {
        this.service = service;
    }

    @GET
    @Path("/name/{name}")
    public Response getCountryByName(@PathParam("name") String n,
                                     @QueryParam("page") Optional<Integer> page,
                                     @QueryParam("size") Optional<Integer> size) {
        var res = service.getCountriesByName(n,page,size);
        return Response.status(res.getStatusCode()).entity(res).build();
    }

}
