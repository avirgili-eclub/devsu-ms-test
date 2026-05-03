package com.devsu.msfinance.infrastructure.rest;

import com.devsu.msfinance.domain.port.in.MovementUseCase;
import com.devsu.msfinance.infrastructure.rest.dto.MovementRequest;
import com.devsu.msfinance.infrastructure.rest.dto.MovementResponse;
import com.devsu.msfinance.infrastructure.rest.dto.StatementResponse;
import com.devsu.msfinance.infrastructure.rest.mapper.MovementMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Movements", description = "Movement registration and account statements")
public class MovementController {

    private final MovementUseCase movementUseCase;
    private final MovementMapper mapper;

    @GetMapping("/api/v1/movements")
    @Operation(summary = "Get all movements")
    @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    public ResponseEntity<List<MovementResponse>> getAll() {
        return ResponseEntity.ok(movementUseCase.getAll().stream().map(mapper::toResponse).toList());
    }

    @GetMapping("/api/v1/movements/{id}")
    @Operation(summary = "Get movement by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movement found"),
            @ApiResponse(responseCode = "404", description = "Movement not found",
                    content = @Content(schema = @Schema(example = "{\"error\": \"Movement not found with id: 1\"}")))
    })
    public ResponseEntity<MovementResponse> getById(
            @Parameter(description = "Movement ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(movementUseCase.getById(id)));
    }

    @PostMapping("/api/v1/movements")
    @Operation(summary = "Register a movement", description = "Registers a DEPOSIT or WITHDRAWAL. Balance is calculated and stored atomically.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Movement registered"),
            @ApiResponse(responseCode = "400", description = "Validation error or insufficient funds",
                    content = @Content(schema = @Schema(example = "{\"error\": \"Insufficient funds: current balance is 100.00, requested withdrawal is 500.00\"}")))
    })
    public ResponseEntity<MovementResponse> create(@Valid @RequestBody MovementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponse(movementUseCase.create(mapper.toDomain(request))));
    }

    @GetMapping("/api/v1/reports")
    @Operation(summary = "Get account statement", description = "Returns all movements for a client within a date range (inclusive).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statement retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date format")
    })
    public ResponseEntity<List<StatementResponse>> getStatement(
            @Parameter(description = "Client ID", example = "1") @RequestParam Long clientId,
            @Parameter(description = "Start date (inclusive)", example = "2024-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "End date (inclusive)", example = "2024-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(movementUseCase.getStatement(clientId, from, to).stream()
                .map(mapper::toStatementResponse)
                .toList());
    }
}
