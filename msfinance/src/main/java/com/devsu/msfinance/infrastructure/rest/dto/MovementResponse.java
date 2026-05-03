package com.devsu.msfinance.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Movement data returned by the API")
public class MovementResponse {

    @Schema(description = "Internal database ID", example = "1")
    private Long id;

    @Schema(description = "Timestamp of the movement", example = "2024-01-15T10:30:00")
    private LocalDateTime date;

    @Schema(description = "Movement type", example = "DEPOSIT")
    private String movementType;

    @Schema(description = "Movement amount", example = "500.00")
    private BigDecimal amount;

    @Schema(description = "Account balance after this movement", example = "1500.00")
    private BigDecimal balance;

    @Schema(description = "Account ID", example = "1")
    private Long accountId;

    @Schema(description = "Account number", example = "A1B2C3D4E5")
    private String accountNumber;
}
