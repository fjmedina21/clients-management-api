package com.myCompany.helpers;

import com.myCompany.models.dtos.ClientCreateRequest;
import com.myCompany.models.dtos.ClientUpdateRequest;
import com.myCompany.models.dtos.ValidationResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientValidator {
    public static boolean isValidCountryCode(String countryCode) {
        String pattern = "^[a-zA-Z]{2}$";
        return Pattern.matches(pattern, countryCode);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^[0-9]{7,15}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern
                .compile("^[a-zA-Z0-9](?:[a-zA-Z0-9._+-]*[a-zA-Z0-9])?@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\\" +
                        ".[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static ValidationResponse canCreateClient(ClientCreateRequest model) {

        List<String> errors = new ArrayList<>();

        if (model.primerNombre() == null) {
            errors.add("Primer Nombre is required");
        }
        if (model.primerApellido() == null) {
            errors.add("Primer Apellido is required");
        }

        if (model.direccion() != null && model.direccion().length() > 200) {
            errors.add("Direccion must be less than 200 characters");
        } else if (model.direccion() == null) {
            errors.add("Direccion is required");
        }

        if (model.correoElectronico() != null && !isValidEmail(model.correoElectronico())) {
            errors.add("Correo Electronico must be a valid email address and can include a +tag " +
                    "before the @ symbol");
        } else if (model.correoElectronico() == null) {
            errors.add("Correo Electronico is required");
        }

        if (model.telefono() != null && !isValidPhoneNumber(model.telefono())) {
            errors.add("Telefono must be a valid phone number");
        } else if (model.telefono() == null) {
            errors.add("Telefono is required");
        }

        if (model.pais() != null && !isValidCountryCode(model.pais())) {
            errors.add("Pais must be a valid ISO 3166-1 alpha-2 code (e.g. DO, US, MX)");
        } else if (model.pais() == null) {
            errors.add("Pais is required");
        }

        return errors.isEmpty()
                ? new ValidationResponse(true, Collections.emptyList())
                : new ValidationResponse(false, errors);
    }

    public static ValidationResponse canUpdateClient(ClientUpdateRequest model) {

        List<String> errors = new ArrayList<>();

        if (model.correoElectronico() != null && !isValidEmail(model.correoElectronico())) {
            errors.add("Correo Electronico must be a valid email address and can include a +tag " +
                    "before the @ symbol");
        } else if (model.correoElectronico() == null) {
            errors.add("Correo Electronico is required");
        }

        if (model.telefono() != null && !isValidPhoneNumber(model.telefono())) {
            errors.add("Telefono must be a valid phone number");
        } else if (model.telefono() == null) {
            errors.add("Telefono is required");
        }

        if (model.pais() != null && !isValidCountryCode(model.pais())) {
            errors.add("Pais must be a valid ISO 3166-1 alpha-2 code (e.g. DO, US, MX)");
        } else if (model.pais() == null) {
            errors.add("Pais is required");
        }

        if (model.direccion() != null && model.direccion().length() > 200) {
            errors.add("Direccion must be less than 200 characters");
        } else if (model.direccion() == null) {
            errors.add("Direccion is required");
        }

        return errors.isEmpty()
                ? new ValidationResponse(true, Collections.emptyList())
                : new ValidationResponse(false, errors);
    }

}
