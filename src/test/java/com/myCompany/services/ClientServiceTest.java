package com.myCompany.services;

import com.myCompany.Repository.ClientRepository;
import com.myCompany.mappers.ClientCreateRequestMapper;
import com.myCompany.mappers.ClientResponseMapper;
import com.myCompany.models.apiResponses.ApiResponse;
import com.myCompany.models.apiResponses.ErrorApiResponse;
import com.myCompany.models.apiResponses.PaginatedApiResponse;
import com.myCompany.models.dtos.ClientCreateRequest;
import com.myCompany.models.dtos.ClientResponse;
import com.myCompany.models.dtos.ClientUpdateRequest;
import com.myCompany.models.entities.Client;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteService - Tests Unitarios")
class ClienteServiceTests extends BaseClientTest {

    @InjectMocks
    ClientService clientService;

    @Mock
    ClientRepository clientRepository;

    @Mock
    ClientResponseMapper clientResponseMapper;

    @Mock
    ClientCreateRequestMapper createRequestMapper;

    @Mock
    ExternalService externalService;

    @Nested
    class CreateClientTests {

        @Test
        void createClient_ValidClient_ReturnCreated() {

            ClientCreateRequest req = validClientCreateRequest();

            Client newclient = new Client();
            newclient.setId(UUID.randomUUID());
            newclient.setPrimerNombre(req.primerNombre());
            newclient.setSegundoNombre(req.segundoNombre());
            newclient.setPrimerApellido(req.primerApellido());
            newclient.setSegundoApellido(req.segundoApellido());
            newclient.setCorreoElectronico(req.correoElectronico());
            newclient.setTelefono(req.telefono());
            newclient.setDireccion(req.direccion());
            newclient.setPais(req.pais());

            ClientResponse res = new ClientResponse(
                    newclient.getId(),
                    newclient.getPrimerNombre(),
                    newclient.getSegundoNombre(),
                    newclient.getPrimerApellido(),
                    newclient.getSegundoApellido(),
                    newclient.getCorreoElectronico(),
                    newclient.getTelefono(),
                    newclient.getDireccion(),
                    newclient.getPais(),
                    demonymDO().demonyms().eng().m()
            );

            Mockito.when(createRequestMapper.apply(req)).thenReturn(newclient);
            Mockito.when(clientResponseMapper.apply(newclient)).thenReturn(res);
            Mockito.when(clientRepository.findByEmail(req.correoElectronico())).thenReturn(Optional.empty());
            Mockito.when(externalService.getDemonymByCode("DO")).thenReturn(Optional.ofNullable(demonymDO()));

            var result = clientService.createClient(req);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.CREATED, result.getStatusCode());
            Assertions.assertInstanceOf(ApiResponse.class, result);

            ApiResponse apiResponse = (ApiResponse) result;
            ClientResponse clientResponse = (ClientResponse) apiResponse.getData();
            Assertions.assertNotNull(clientResponse);
        }

        @Test
        void createClient_InvalidClient_ReturnBadRequest() {

            ClientCreateRequest req = clientIsMissingRequiredData();

            var result = clientService.createClient(req);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.BAD_REQUEST, result.getStatusCode());
            Assertions.assertInstanceOf(ErrorApiResponse.class, result);
        }

        @Test
        void createClient_DuplicateEmail_ReturnConflict() {

            ClientCreateRequest req = validClientCreateRequest2();
            Client existing = new Client();
            existing.setId(UUID.randomUUID());
            existing.setCorreoElectronico(req.correoElectronico());

            Mockito.when(clientRepository.findByEmail(req.correoElectronico())).thenReturn(Optional.of(existing));
            var result = clientService.createClient(req);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.CONFLICT, result.getStatusCode());
            Assertions.assertInstanceOf(ErrorApiResponse.class, result);
        }

        @Test
        void createClient_InvalidCountry_ReturnBadRequest() {

            ClientCreateRequest req = clientWithInvalidISO3166Alpha2CountryCode();

            var result = clientService.createClient(req);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.BAD_REQUEST, result.getStatusCode());
            Assertions.assertInstanceOf(ErrorApiResponse.class, result);
        }

        @Test
        void createClient_InvalidPhoneNumber_ReturnBadRequest() {

            ClientCreateRequest req = clientWithInvalidPhoneNumber();

            var result = clientService.createClient(req);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.BAD_REQUEST, result.getStatusCode());
            Assertions.assertInstanceOf(ErrorApiResponse.class, result);
        }

        @Test
        void createClient_InvalidEmail_ReturnBadRequest() {

            ClientCreateRequest req = clientWithInvalidEmail();

            var result = clientService.createClient(req);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.BAD_REQUEST, result.getStatusCode());
            Assertions.assertInstanceOf(ErrorApiResponse.class, result);


        }
    }

    @Nested
    class GetClientsTests {

        @Test
        void getClients_ReturnData() {
            List<Client> clients = clientList();

            Mockito.when(clientRepository.listAllClients()).thenReturn(clients);

            var result = clientService.getClients(Optional.empty(), Optional.empty(), Optional.empty());

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.OK, result.getStatusCode());
        }

        @Test
        void getClients_ReturnEmptyList() {
            List<Client> clients = clientEmptyList();

            Mockito.when(clientRepository.listAllClients()).thenReturn(clients);

            var result = clientService.getClients(Optional.empty(), Optional.empty(), Optional.empty());

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.OK, result.getStatusCode());
        }

        @Test
        void getClients_FilterByCountry_OnlyReturnDataFromCountry() {
            List<Client> clients = clientList();
            List<ClientResponse> mappedClients = clientResponseList();

            Mockito.when(clientRepository.listAllClients()).thenReturn(clients);

            for (int i = 0; i < clients.size(); i++) {

                Client client = clients.get(i);
                ClientResponse response = mappedClients.get(i);

                Mockito.when(clientResponseMapper.apply(client))
                        .thenReturn(response);
            }
            var result = clientService.getClients(Optional.of("DO"), Optional.empty(), Optional.empty());

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.OK, result.getStatusCode());

            PaginatedApiResponse<ClientResponse> apiResponse = (PaginatedApiResponse<ClientResponse>) result;
            Assertions.assertNotNull(apiResponse);

            List<ClientResponse> data = apiResponse.getData();
            Assertions.assertTrue(
                    data.stream().allMatch(x -> x.pais().equalsIgnoreCase("DO"))
            );
        }

        @Test
        void getClients_FilterByCountry_ReturnZeroDataFromCountry() {
            List<Client> clients = clientList();
            List<ClientResponse> mappedClients = clientResponseList();

            Mockito.when(clientRepository.listAllClients()).thenReturn(clients);

            for (int i = 0; i < clients.size(); i++) {

                Client client = clients.get(i);
                ClientResponse response = mappedClients.get(i);

                Mockito.when(clientResponseMapper.apply(client))
                        .thenReturn(response);
            }
            var result = clientService.getClients(Optional.of("JP"), Optional.empty(), Optional.empty());

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.OK, result.getStatusCode());

            PaginatedApiResponse<ClientResponse> apiResponse = (PaginatedApiResponse<ClientResponse>) result;
            Assertions.assertNotNull(apiResponse);

            List<ClientResponse> data = apiResponse.getData();
            Assertions.assertTrue(
                    data.stream().allMatch(x -> x.pais().equalsIgnoreCase("JP"))
            );
        }
    }

    @Nested
    class GetClientByIdTests {

        @Test
        void getClientByID_Exist_ReturnOk() {
            Client existing = new Client();
            existing.setId(UUID.randomUUID());

            ClientResponse mappedExisting = new ClientResponse(
                    existing.getId(),
                    "Juan", null, "Perez", null,
                    "", "", "", "DO", ""
            );

            Mockito.when(clientRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
            Mockito.when(clientResponseMapper.apply(existing)).thenReturn(mappedExisting);

            var result = clientService.getClientById(existing.getId());

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.OK, result.getStatusCode());
            Assertions.assertInstanceOf(ApiResponse.class, result);

            ApiResponse<ClientResponse> apiResponse = (ApiResponse<ClientResponse>) result;
            ClientResponse data = apiResponse.getData();
            Assertions.assertNotNull(data);

        }

        @Test
        void getClientByID_NotExist_ReturnNotFound() {
            UUID nonExistingId = UUID.randomUUID();

            Mockito.when(clientRepository.findById(nonExistingId)).thenReturn(Optional.ofNullable(null));

            var result = clientService.getClientById(nonExistingId);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.NOT_FOUND, result.getStatusCode());
            Assertions.assertInstanceOf(ErrorApiResponse.class, result);
        }

    }

    @Nested
    class updateClientTests {

        @Test
        void updateClient_ReturnOk() {
            ClientUpdateRequest updReq = validClientUpdateRequest();

            Client existing = new Client();
            existing.setId(UUID.randomUUID());
            existing.setCorreoElectronico("test@domain.com");
            existing.setPais("US");
            existing.setGentilicio("American");
            existing.setDireccion("123 Main St");
            existing.setTelefono("55512343092");

            ClientResponse mappedExisting = new ClientResponse(
                    existing.getId(),
                    existing.getPrimerNombre(),
                    existing.getSegundoNombre(),
                    existing.getPrimerApellido(),
                    existing.getSegundoApellido(),
                    updReq.correoElectronico(),
                    updReq.direccion(),
                    updReq.telefono(),
                    updReq.pais(),
                    demonymDO().demonyms().eng().m()
            );

            Mockito.when(clientRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
            Mockito.when(clientRepository.canUpdateEmail(existing.getId(), updReq.correoElectronico())).thenReturn(true);
            Mockito.when(externalService.getDemonymByCode(updReq.pais())).thenReturn(Optional.of(demonymDO()));
            Mockito.when(clientResponseMapper.apply(existing)).thenReturn(mappedExisting);

            var result = clientService.updateClient(existing.getId(), updReq);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.OK, result.getStatusCode());
            Assertions.assertInstanceOf(ApiResponse.class, result);

            ApiResponse<ClientResponse> apiResponse = (ApiResponse<ClientResponse>) result;
            ClientResponse data = apiResponse.getData();

            Assertions.assertNotNull(data);
            Assertions.assertEquals(updReq.correoElectronico(), data.correoElectronico());
            Assertions.assertEquals(updReq.pais(), data.pais());
            Assertions.assertEquals(updReq.telefono(), data.telefono());
            Assertions.assertEquals(updReq.direccion(), data.direccion());
            Assertions.assertEquals(demonymDO().demonyms().eng().m(), data.gentilicio());
        }

        @Test
        void updateClient_ChangeCountry_ProduceDemonymChange() {
            ClientUpdateRequest updReq = validClientUpdateRequest();

            Client existing = new Client();
            existing.setId(UUID.randomUUID());
            existing.setPais("DO");
            existing.setGentilicio("Dominican");

            ClientResponse mappedExisting = new ClientResponse(
                    existing.getId(),
                    existing.getPrimerNombre(),
                    existing.getSegundoNombre(),
                    existing.getPrimerApellido(),
                    existing.getSegundoApellido(),
                    updReq.correoElectronico(),
                    updReq.direccion(),
                    updReq.telefono(),
                    updReq.pais(),
                    demonymUS().demonyms().eng().m()
            );

            Mockito.when(clientRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
            Mockito.when(clientRepository.canUpdateEmail(existing.getId(), updReq.correoElectronico())).thenReturn(true);
            Mockito.when(externalService.getDemonymByCode(updReq.pais())).thenReturn(Optional.of(demonymDO()));
            Mockito.when(clientResponseMapper.apply(existing)).thenReturn(mappedExisting);

            var result = clientService.updateClient(existing.getId(), updReq);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.OK, result.getStatusCode());

            ApiResponse<ClientResponse> apiResponse = (ApiResponse<ClientResponse>) result;
            ClientResponse data = apiResponse.getData();

            Assertions.assertNotNull(data);
            Assertions.assertEquals(demonymUS().demonyms().eng().m(), data.gentilicio());
        }

        @Test
        void updateClient_SameEmail_DoNotProduceConflict() {
            ClientUpdateRequest updReq = validClientUpdateRequest();

            Client existing = new Client();
            existing.setId(UUID.randomUUID());
            existing.setCorreoElectronico(updReq.correoElectronico());

            ClientResponse mappedExisting = new ClientResponse(
                    existing.getId(),
                    existing.getPrimerNombre(),
                    existing.getSegundoNombre(),
                    existing.getPrimerApellido(),
                    existing.getSegundoApellido(),
                    updReq.correoElectronico(),
                    updReq.direccion(),
                    updReq.telefono(),
                    updReq.pais(),
                    demonymUS().demonyms().eng().m()
            );

            Mockito.when(clientRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
            Mockito.when(clientRepository.canUpdateEmail(existing.getId(), updReq.correoElectronico())).thenReturn(true);
            Mockito.when(externalService.getDemonymByCode(updReq.pais())).thenReturn(Optional.of(demonymDO()));
            Mockito.when(clientResponseMapper.apply(existing)).thenReturn(mappedExisting);

            var result = clientService.updateClient(existing.getId(), updReq);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.OK, result.getStatusCode());

            ApiResponse<ClientResponse> apiResponse = (ApiResponse<ClientResponse>) result;
            ClientResponse data = apiResponse.getData();

            Assertions.assertNotNull(data);
            Assertions.assertEquals(updReq.correoElectronico(), data.correoElectronico());

        }

        @Test
        void updateClient_NonExisting_ReturnBadRequest() {
            UUID nonExistingId = UUID.randomUUID();

            ClientUpdateRequest updReq = validClientUpdateRequest();

            Mockito.when(clientRepository.findById(nonExistingId)).thenReturn(Optional.ofNullable(null));

            var result = clientService.updateClient(nonExistingId, updReq);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.BAD_REQUEST, result.getStatusCode());
            Assertions.assertInstanceOf(ErrorApiResponse.class, result);
        }

        @Test
        void updateClient_DuplicatedEmail_ProduceConflict() {
            UUID updReqId = UUID.randomUUID();
            ClientUpdateRequest updReqWithDuplicatedEmail = validClientUpdateRequest();

            Client existing = new Client();
            existing.setId(UUID.randomUUID());
            existing.setCorreoElectronico(updReqWithDuplicatedEmail.correoElectronico());

            Mockito.when(clientRepository.findById(updReqId)).thenReturn(Optional.of(existing));
            Mockito.when(clientRepository.canUpdateEmail(updReqId, updReqWithDuplicatedEmail.correoElectronico())).thenReturn(false);

            var result = clientService.updateClient(updReqId, updReqWithDuplicatedEmail);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.CONFLICT, result.getStatusCode());
            Assertions.assertInstanceOf(ErrorApiResponse.class, result);
        }

    }

    @Nested
    class DeleteClientTests {

        @Test
        void deleteClient_Exist_ReturnNoContent() {
            Client existing = new Client();
            existing.setId(UUID.randomUUID());

            Mockito.when(clientRepository.findById(existing.getId())).thenReturn(Optional.of(existing));

            var result = clientService.deleteClient(existing.getId());

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.NO_CONTENT, result.getStatusCode());
            Assertions.assertInstanceOf(ApiResponse.class, result);
        }

        @Test
        void deleteClient_NotExist_ReturnBadRequest() {
            UUID nonExistingId = UUID.randomUUID();

            Mockito.when(clientRepository.findById(nonExistingId)).thenReturn(Optional.ofNullable(null));

            var result = clientService.deleteClient(nonExistingId);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(RestResponse.StatusCode.BAD_REQUEST, result.getStatusCode());
            Assertions.assertInstanceOf(ErrorApiResponse.class, result);
        }
    }


}

