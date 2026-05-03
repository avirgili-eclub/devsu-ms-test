package com.devsu.msfinance.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Payload for registering a movement")
public class MovementRequest {

    @NotNull
    @Schema(description = "ID of the account to move funds on", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long accountId;

    @NotBlank
    @Schema(description = "Movement type", example = "DEPOSIT", allowableValues = {"DEPOSIT", "WITHDRAWAL"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String movementType;

    @NotNull
    @DecimalMin("0.01")
    @Schema(description = "Amount (always positive — direction is determined by movementType)", example = "500.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;
}
