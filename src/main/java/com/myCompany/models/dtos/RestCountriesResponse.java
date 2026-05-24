package com.myCompany.models.dtos;

public record RestCountriesResponse(
        Name name,
        Demonyms demonyms,
        String cca2
) {

    public record Name(
            String common,
            String official
    ) {
    }

    public record Demonyms(
            Demonym eng
    ) {
    }

    public record Demonym(
            String f,
            String m
    ) {
    }
}