package com.devsu.msfinance.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Payload for creating or updating an account")
public class AccountRequest {

    @NotBlank
    @Schema(description = "Account type", example = "SAVINGS", allowableValues = {"SAVINGS", "CHECKING"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String accountType;

    @NotNull
    @DecimalMin("0.00")
    @Schema(description = "Initial balance — becomes the starting current balance", example = "1000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal initialBalance;

    @NotNull
    @Schema(description = "Account active status", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean status;

    @NotNull
    @Schema(description = "Owner client ID (from msidentity)", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long clientId;
}
