package com.devsu.msfinance.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Account statement entry — one row per movement")
public class StatementResponse {

    @Schema(description = "Movement timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime date;

    @Schema(description = "Client ID", example = "1")
    private Long clientId;

    @Schema(description = "Account number", example = "A1B2C3D4E5")
    private String accountNumber;

    @Schema(description = "Account type", example = "SAVINGS")
    private String accountType;

    @Schema(description = "Initial balance of the account", example = "1000.00")
    private BigDecimal initialBalance;

    @Schema(description = "Account active status", example = "true")
    private boolean status;

    @Schema(description = "Movement type", example = "DEPOSIT")
    private String movementType;

    @Schema(description = "Movement amount", example = "500.00")
    private BigDecimal amount;

    @Schema(description = "Balance after movement", example = "1500.00")
    private BigDecimal balance;
}
