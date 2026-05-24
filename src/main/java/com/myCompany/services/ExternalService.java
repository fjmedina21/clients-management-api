package com.myCompany.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myCompany.helpers.PagedList;
import com.myCompany.models.apiResponses.BaseApiResponse;
import com.myCompany.models.apiResponses.ErrorApiResponse;
import com.myCompany.models.apiResponses.PaginatedApiResponse;
import com.myCompany.models.dtos.RestCountriesResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.resource.spi.UnavailableException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import static com.myCompany.helpers.PagedList.toPagedList;

@ApplicationScoped
public class ExternalService {

    private static final String URL = "https://restcountries.com";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ExternalService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Optional<RestCountriesResponse> getDemonymByCode(String countryCode) {

        URI endpoint = URI.create(String.format("%s/v3.1/alpha/%s", URL, countryCode));
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(endpoint).GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = httpClient
                    .send(request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (response.statusCode() != 200) {
                return Optional.empty();
            }

            List<RestCountriesResponse> countries = objectMapper
                    .readValue(response.body(),
                            new TypeReference<>() {
                            }
                    );

            return Optional.ofNullable(countries.getFirst());

        } catch (IOException | InterruptedException ex) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    public BaseApiResponse getCountriesByName(String name, Optional<Integer> page, Optional<Integer> size) {
        URI endpoint = URI.create(String.format("%s/v3.1/name/%s", URL, name));

        try {
            HttpRequest request = HttpRequest.newBuilder().uri(endpoint).GET()
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {

                return new ErrorApiResponse(response.statusCode(), "Error fetching data from Rest Countries API");
            }

            List<RestCountriesResponse> countries = objectMapper.readValue(response.body(),
                    new TypeReference<>() {
                    });

            var pagedData = toPagedList(countries, page.orElse(1), size.orElse(25));
            return new PaginatedApiResponse<>(pagedData);
        } catch (IOException | InterruptedException ex) {
            Thread.currentThread().interrupt();
            return new ErrorApiResponse(503, "Service Unavailable");
        }
    }

}
