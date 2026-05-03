package com.devsu.msidentity.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Client data returned by the API")
public class ClientResponse {

    @Schema(description = "Internal database ID", example = "1")
    private Long id;

    @Schema(description = "Business identifier (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
    private String clientId;

    @Schema(description = "Full name", example = "John Doe")
    private String name;

    @Schema(description = "Gender", example = "Male")
    private String gender;

    @Schema(description = "Age", example = "30")
    private int age;

    @Schema(description = "National ID or passport number", example = "1234567890")
    private String identification;

    @Schema(description = "Home address", example = "123 Main St, Springfield")
    private String address;

    @Schema(description = "Phone number", example = "+1-555-0100")
    private String phone;

    @Schema(description = "Account active status", example = "true")
    private boolean status;
}
