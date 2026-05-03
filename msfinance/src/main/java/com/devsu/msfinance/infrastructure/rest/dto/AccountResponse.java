package com.devsu.msfinance.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Account data returned by the API")
public class AccountResponse {

    @Schema(description = "Internal database ID", example = "1")
    private Long id;

    @Schema(description = "Auto-generated account number", example = "A1B2C3D4E5")
    private String accountNumber;

    @Schema(description = "Account type", example = "SAVINGS")
    private String accountType;

    @Schema(description = "Balance when the account was opened", example = "1000.00")
    private BigDecimal initialBalance;

    @Schema(description = "Current balance after all movements", example = "1350.00")
    private BigDecimal currentBalance;

    @Schema(description = "Account active status", example = "true")
    private boolean status;

    @Schema(description = "Owner client ID", example = "1")
    private Long clientId;
}
