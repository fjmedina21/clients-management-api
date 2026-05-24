package com.myCompany.models.dtos;

import jakarta.annotation.Nullable;

import java.util.UUID;

public record ClientResponse(
        UUID id,
        String primerNombre,
        @Nullable String segundoNombre,
        String primerApellido,
        @Nullable String segundoApellido,
        String correoElectronico,
        String direccion,
        String telefono,
        String pais,
        String gentilicio
) {
}
