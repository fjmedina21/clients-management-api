package com.myCompany.services;

import com.myCompany.models.dtos.ClientCreateRequest;
import com.myCompany.models.dtos.ClientResponse;
import com.myCompany.models.dtos.ClientUpdateRequest;
import com.myCompany.models.dtos.RestCountriesResponse;
import com.myCompany.models.entities.Client;

import java.util.List;
import java.util.UUID;

public abstract class BaseClientTest {

    // ──────────────────────────────────────────────
    // Fixtures de datos válidos
    // ──────────────────────────────────────────────

    protected ClientCreateRequest validClientCreateRequest() {
        return new ClientCreateRequest(
                "Juan", null,
                "Perez", null,
                "juan.perez@domain.com", "8292550000",
                "Santo Domingo", "DO"
        );
    }

    protected ClientCreateRequest validClientCreateRequest2() {
        return new ClientCreateRequest(
                "María", "Jose", "López", null,
                "mariaj.lopez@ejemplo.com", "5551234567",
                "Av. Reforma 100, CDMX", "MX"
        );
    }

    protected ClientUpdateRequest validClientUpdateRequest() {
        return new ClientUpdateRequest(
                "nuevo@ejemplo.com", "8097654321",
                "Nueva Dirección 123", "DO"
        );
    }

    protected ClientUpdateRequest validClientUpdateRequest1() {
        return new ClientUpdateRequest(
                "nuevo@ejemplo.com", "55512343092",
                "123 Main St", "US"
        );
    }

    // ──────────────────────────────────────────────
    // Fixtures de datos inválidos
    // ──────────────────────────────────────────────

    protected ClientCreateRequest clientIsMissingRequiredData() {
        return new ClientCreateRequest(
                null, "Javier", null, "Cabrera",
                null, null,
                null, null
        );
    }

    protected ClientCreateRequest clientWithInvalidEmail() {
        return new ClientCreateRequest(
                "Pedro", null, "Ramírez", "Cabrera",
                "pedroramirez.c+@ejemplo.com", "8091111111",
                "Calle 1", "DO"
        );
    }

    protected ClientCreateRequest clientWithInvalidISO3166Alpha2CountryCode() {
        return new ClientCreateRequest(
                "Pedro", "Jose", "Ramírez", "Cabrera",
                "pedro.cabrera+test@ejemplo.com", "8091111111",
                "Calle 1", "DO"
        );
    }

    protected ClientCreateRequest clientWithInvalidPhoneNumber() {
        return new ClientCreateRequest(
                "Pedro", "Jose", "Ramírez", "Cabrera",
                "pedro.cabrera+test@ejemplo.com", "809 111 1111",
                "Calle 1", "DO"
        );
    }

    protected ClientCreateRequest clientWithInvalidPhoneNumber1() {
        return new ClientCreateRequest(
                "Pedro", "Jose", "Ramírez", "Cabrera",
                "pedro.cabrera+test@ejemplo.com", "809-111-1111",
                "Calle 1", "DO"
        );
    }


    protected ClientCreateRequest clientWithInvalidPhoneNumber2() {
        return new ClientCreateRequest(
                "Pedro", "Jose", "Ramírez", "Cabrera",
                "pedro.cabrera+test@ejemplo.com", "809 111-1111",
                "Calle 1", "DO"
        );
    }

    protected ClientCreateRequest clientWithInvalidPhoneNumber3() {
        return new ClientCreateRequest(
                "Pedro", "Jose", "Ramírez", "Cabrera",
                "pedro.cabrera+test@ejemplo.com", "809123",
                "Calle 1", "DO"
        );
    }

    protected ClientCreateRequest clientWithInvalidPhoneNumber4() {
        return new ClientCreateRequest(
                "Pedro", "Jose", "Ramírez", "Cabrera",
                "pedro.cabrera+test@ejemplo.com", "8091235678123450",
                "Calle 1", "DO"
        );
    }

    protected ClientCreateRequest clientIsMissingEmail() {
        return new ClientCreateRequest(
                "Pedro", "Jose", "Ramírez", "Cabrera",
                null, "8091111111",
                "Calle 1", "DO"
        );
    }

    protected ClientCreateRequest clientIsMissingCountry() {
        return new ClientCreateRequest(
                "Pedro", "Jose", "Ramírez", "Cabrera",
                "Pedroj.ramirez+dev@ejemplo.com", "8091111111",
                "Calle 1", null
        );
    }

    protected ClientUpdateRequest updateClientWithInvalidEmail() {
        return new ClientUpdateRequest(
                "correo-malo.@gmail.com", "8097654321",
                "Santo Domingo", "DO"
        );
    }

    protected ClientUpdateRequest updateClientWithInvalidCountryCode() {
        return new ClientUpdateRequest(
                "usuario@domain.com", "8097654321",
                "Santo Domingo", "DOM"
        );
    }

    // ──────────────────────────────────────────────
    // Fixtures de una lista de datos
    // ──────────────────────────────────────────────

    protected List<Client> clientEmptyList() {
        return List.of();
    }


    protected List<Client> clientList() {
        Client client1 = new Client();
        client1.setId(UUID.randomUUID());
        client1.setPais("DO");
        Client client2 = new Client();
        client2.setId(UUID.randomUUID());
        client2.setPais("DO");
        Client client3 = new Client();
        client3.setId(UUID.randomUUID());
        client3.setPais("US");
        Client client4 = new Client();
        client4.setId(UUID.randomUUID());
        client4.setPais("ES");
        Client client5 = new Client();
        client5.setId(UUID.randomUUID());
        client5.setPais("BR");
        return List.of(client1, client2, client3, client4, client5);
    }

    protected List<ClientResponse> clientResponseList() {
        ClientResponse client1 = new ClientResponse(
                UUID.randomUUID(),
                "Juan", null, "Perez", null,
                "", "", "", "DO", ""
        );
        ClientResponse client2 = new ClientResponse(
                UUID.randomUUID(),
                "Juan", null, "Perez", null,
                "", "", "", "DO", ""
        );

        ClientResponse client3 = new ClientResponse(
                UUID.randomUUID(),
                "Juan", null, "Perez", null,
                "", "", "", "US", ""
        );
        ClientResponse client4 = new ClientResponse(
                UUID.randomUUID(),
                "Juan", null, "Perez", null,
                "", "", "", "ES", ""
        );
        ClientResponse client5 = new ClientResponse(
                UUID.randomUUID(),
                "Juan", null, "Perez", null,
                "", "", "", "BR", ""
        );

        return List.of(client1, client2, client3, client4, client5);
    }

    public RestCountriesResponse demonymDO() {
        return new
                RestCountriesResponse(
                new RestCountriesResponse.Name("DO", "DO"),
                new RestCountriesResponse.Demonyms(
                        new RestCountriesResponse.Demonym("Dominican", "Dominican")),
                "DO");
    }

    public RestCountriesResponse demonymUS() {
        return new
                RestCountriesResponse(
                new RestCountriesResponse.Name("United States", "United States"),
                new RestCountriesResponse.Demonyms(
                        new RestCountriesResponse.Demonym("American", "American")),
                "US");
    }
}
