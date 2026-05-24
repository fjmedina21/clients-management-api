package com.myCompany.models.dtos;

public record ClientUpdateRequest(
        String correoElectronico,
        String telefono,
        String direccion,
        String pais) {
}

