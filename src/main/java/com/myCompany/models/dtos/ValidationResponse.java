package com.myCompany.models.dtos;

import java.util.List;

public record ValidationResponse(boolean isValid, List<String> errors) {
}
