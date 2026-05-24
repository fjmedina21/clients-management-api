package com.myCompany.services;

import com.myCompany.Repository.ClientRepository;
import com.myCompany.helpers.ClientValidator;
import com.myCompany.mappers.ClientCreateRequestMapper;
import com.myCompany.mappers.ClientResponseMapper;
import com.myCompany.models.apiResponses.ApiResponse;
import com.myCompany.models.apiResponses.BaseApiResponse;
import com.myCompany.models.apiResponses.ErrorApiResponse;
import com.myCompany.models.apiResponses.PaginatedApiResponse;
import com.myCompany.models.dtos.*;
import com.myCompany.models.entities.Client;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.myCompany.helpers.PagedList.toPagedList;

@ApplicationScoped
@Transactional
public class ClientService implements PanacheRepository<Client> {

    private final ClientRepository clientRepository;
    private final ClientResponseMapper clientResponseMapper;
    private final ClientCreateRequestMapper createRequestMapper;
    private final ExternalService externalService;

    @Inject
    public ClientService(ClientRepository clientRepository, ClientResponseMapper clientResponseMapper, ClientCreateRequestMapper createRequestMapper, ExternalService externalService) {
        this.clientRepository = clientRepository;
        this.clientResponseMapper = clientResponseMapper;
        this.createRequestMapper = createRequestMapper;
        this.externalService = externalService;
    }

    public BaseApiResponse getClients(
            Optional<String> country,
            Optional<Integer> page,
            Optional<Integer> size) {

        List<ClientResponse> data = clientRepository.listAllClients().stream()
                .map(clientResponseMapper).collect(Collectors.toList());

        if (country.isPresent()) {
            data = data.stream()
                    .filter(p -> p.pais().equalsIgnoreCase(country.get()))
                    .collect(Collectors.toList());
        }

        var pagedData = toPagedList(data, page.orElse(1), size.orElse(25));
        return new PaginatedApiResponse<>(pagedData);
    }

    public BaseApiResponse getClientById(UUID id) {
        var data = clientRepository.findById(id).map(clientResponseMapper).orElse(null);
        if (data == null) return new ErrorApiResponse(404, "Not Found");
        return new ApiResponse<>(data);
    }

    public BaseApiResponse createClient(ClientCreateRequest model) {
        ValidationResponse x = ClientValidator.canCreateClient(model);
        if (!x.isValid()) return new ErrorApiResponse(400, "Bad Request", x.errors());

        var exist = clientRepository.findByEmail(model.correoElectronico());
        if (exist.isPresent()) return new ErrorApiResponse(409, "Conflict", List.of("Correo Electrónico en uso"));

        var client = createRequestMapper.apply(model);
        Optional<RestCountriesResponse> country = externalService.getDemonymByCode(model.pais());
        if (country.isEmpty())
            return new ErrorApiResponse(400, "Validation Failed", List.of("Pais no fue encontrado en Rest Countries API"));

        client.setGentilicio(country.map(c -> c.demonyms().eng().m()).orElse("desconocido"));
        clientRepository.save(client);

        if (client.getId() == null) return new ErrorApiResponse(400, "Creation Failed");

        return new ApiResponse<>(RestResponse.StatusCode.CREATED, clientResponseMapper.apply(client));
    }

    public BaseApiResponse updateClient(UUID id, ClientUpdateRequest model) {
        ValidationResponse x = ClientValidator.canUpdateClient(model);
        if (!x.isValid()) return new ErrorApiResponse(400, "Validation Failed", x.errors());

        var client = clientRepository.findById(id).orElse(null);
        if (client == null) return new ErrorApiResponse(400, "Bad Request");

        if (!clientRepository.canUpdateEmail(id, model.correoElectronico()))
            return new ErrorApiResponse(409, "Conflict", List.of("Correo Electrónico en uso"));


        client.setCorreoElectronico(model.correoElectronico());
        client.setDireccion(model.direccion());
        client.setTelefono(model.telefono());
        client.setPais(model.pais());

        var country = externalService.getDemonymByCode(model.pais());
        if (country.isEmpty()) {
            return new ErrorApiResponse(400, "Bad Request", List.of("Pais no fue encontrado en Rest Countries API"));
        }

        client.setGentilicio(country.map(c -> c.demonyms().eng().m()).orElse("desconocido"));
        var mappedClient = clientResponseMapper.apply(client);
        return new ApiResponse<>(mappedClient);
    }

    public BaseApiResponse deleteClient(UUID id) {
        var client = clientRepository.findById(id).orElse(null);
        if (client == null) return new ErrorApiResponse(400, "Bad Request");

        client.setDeletedAt(java.time.Instant.now());
        return new ApiResponse<>(204);
    }

}
