package com.myCompany.mappers;

import com.myCompany.models.dtos.ClientResponse;
import com.myCompany.models.entities.Client;
import jakarta.enterprise.context.RequestScoped;

import java.util.function.Function;

@RequestScoped
public class ClientResponseMapper implements Function<Client, ClientResponse> {

    @Override
    public ClientResponse apply(Client entity) {
        return new ClientResponse(
                entity.getId(),
                entity.getPrimerNombre(),
                String.valueOf(entity.getSegundoNombre()),
                entity.getPrimerApellido(),
                String.valueOf(entity.getSegundoApellido()),
                entity.getCorreoElectronico(),
                entity.getDireccion(),
                entity.getTelefono(),
                entity.getPais(),
                entity.getGentilicio()
        );
    }
}
