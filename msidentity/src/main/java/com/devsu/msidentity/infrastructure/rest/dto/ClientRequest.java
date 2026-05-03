package com.devsu.msidentity.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Payload for creating or updating a client")
public class ClientRequest {

    @NotBlank
    @Schema(description = "Full name", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Gender", example = "Male")
    private String gender;

    @Min(15)
    @Schema(description = "Age (minimum 15)", example = "30")
    private int age;

    @NotBlank
    @Schema(description = "National ID or passport number — must be unique", example = "1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
    private String identification;

    @Schema(description = "Home address", example = "123 Main St, Springfield")
    private String address;

    @Schema(description = "Phone number", example = "+1-555-0100")
    private String phone;

    @Schema(description = "Plain-text password (hashed on server). Omit on update to keep current password.", example = "SecurePass123")
    private String password;

    @NotNull
    @Schema(description = "Account active status", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean status;
}
