package com.myCompany.mappers;

import com.myCompany.models.dtos.ClientCreateRequest;
import com.myCompany.models.entities.Client;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class ClientCreateRequestMapper implements Function<ClientCreateRequest, Client> {

    @Override
    public Client apply(ClientCreateRequest dto) {
        var entity = new Client();

        entity.setPrimerNombre(dto.primerNombre().trim());
        if (dto.segundoNombre() != null) {
            entity.setSegundoNombre(dto.segundoNombre().trim());
        }
        entity.setPrimerApellido(dto.primerApellido().trim());
        if (dto.segundoApellido() != null) {
            entity.setSegundoApellido(dto.segundoApellido().trim());
        }
        entity.setCorreoElectronico(dto.correoElectronico().trim());
        entity.setDireccion(dto.direccion().trim());
        entity.setTelefono(dto.telefono().trim());
        entity.setPais(dto.pais().trim());

        return entity;
    }
}
