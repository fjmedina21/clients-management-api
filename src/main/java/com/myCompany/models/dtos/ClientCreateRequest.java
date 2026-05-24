package com.myCompany.models.dtos;

import jakarta.annotation.Nullable;

public record ClientCreateRequest(
        String primerNombre,
        @Nullable String segundoNombre,
        String primerApellido,
        @Nullable String segundoApellido,
        String correoElectronico,
        String telefono,
        String direccion,
        String pais) {
}
