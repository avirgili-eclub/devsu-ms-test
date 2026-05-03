package com.devsu.msfinance.infrastructure.rest;

import com.devsu.msfinance.domain.port.in.AccountUseCase;
import com.devsu.msfinance.infrastructure.rest.dto.AccountRequest;
import com.devsu.msfinance.infrastructure.rest.dto.AccountResponse;
import com.devsu.msfinance.infrastructure.rest.mapper.AccountMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "CRUD operations for bank accounts")
public class AccountController {

    private final AccountUseCase accountUseCase;
    private final AccountMapper mapper;

    @GetMapping
    @Operation(summary = "Get all accounts")
    @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    public ResponseEntity<List<AccountResponse>> getAll() {
        return ResponseEntity.ok(accountUseCase.getAll().stream().map(mapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(schema = @Schema(example = "{\"error\": \"Account not found with id: 1\"}")))
    })
    public ResponseEntity<AccountResponse> getById(
            @Parameter(description = "Account ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(accountUseCase.getById(id)));
    }

    @PostMapping
    @Operation(summary = "Create a new account", description = "Account number is auto-generated. Current balance starts equal to initial balance.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created"),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(example = "{\"error\": \"accountType: must not be blank\"}")))
    })
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody AccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponse(accountUseCase.create(mapper.toDomain(request))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an account", description = "Updates account type, status and client ID. Balance and account number are immutable.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account updated"),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(schema = @Schema(example = "{\"error\": \"Account not found with id: 1\"}")))
    })
    public ResponseEntity<AccountResponse> update(
            @Parameter(description = "Account ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody AccountRequest request) {
        return ResponseEntity.ok(mapper.toResponse(accountUseCase.update(id, mapper.toDomain(request))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an account")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Account deleted"),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(schema = @Schema(example = "{\"error\": \"Account not found with id: 1\"}")))
    })
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Account ID", example = "1") @PathVariable Long id) {
        accountUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
