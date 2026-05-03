package com.devsu.msidentity.infrastructure.rest;

import com.devsu.msidentity.domain.port.in.ClientUseCase;
import com.devsu.msidentity.infrastructure.rest.dto.ClientRequest;
import com.devsu.msidentity.infrastructure.rest.dto.ClientResponse;
import com.devsu.msidentity.infrastructure.rest.mapper.ClientMapper;
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
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "CRUD operations for client identity management")
public class ClientController {

    private final ClientUseCase clientUseCase;
    private final ClientMapper mapper;

    @GetMapping
    @Operation(summary = "Get all clients", description = "Returns the full list of registered clients")
    @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    public ResponseEntity<List<ClientResponse>> getAll() {
        return ResponseEntity.ok(clientUseCase.getAll().stream()
                .map(mapper::toResponse)
                .toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get client by ID", description = "Returns a single client by their internal database ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client found"),
            @ApiResponse(responseCode = "404", description = "Client not found",
                    content = @Content(schema = @Schema(example = "{\"error\": \"Client not found with id: 1\"}")))
    })
    public ResponseEntity<ClientResponse> getById(
            @Parameter(description = "Internal client ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(clientUseCase.getById(id)));
    }

    @PostMapping
    @Operation(summary = "Create a new client", description = "Creates a client. Password is hashed server-side. A UUID clientId is auto-generated.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client created"),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(example = "{\"error\": \"name: must not be blank\"}"))),
            @ApiResponse(responseCode = "409", description = "Identification already registered",
                    content = @Content(schema = @Schema(example = "{\"error\": \"Identification already registered: 1234567890\"}")))
    })
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponse(clientUseCase.create(mapper.toDomain(request))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a client", description = "Full update of client data. Omit password field to keep the current one.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client updated"),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(example = "{\"error\": \"name: must not be blank\"}"))),
            @ApiResponse(responseCode = "404", description = "Client not found",
                    content = @Content(schema = @Schema(example = "{\"error\": \"Client not found with id: 1\"}")))
    })
    public ResponseEntity<ClientResponse> update(
            @Parameter(description = "Internal client ID", example = "1") @PathVariable Long id,
            @Valid @RequestBody ClientRequest request) {
        return ResponseEntity.ok(mapper.toResponse(clientUseCase.update(id, mapper.toDomain(request))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a client", description = "Permanently deletes a client by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client deleted"),
            @ApiResponse(responseCode = "404", description = "Client not found",
                    content = @Content(schema = @Schema(example = "{\"error\": \"Client not found with id: 1\"}")))
    })
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Internal client ID", example = "1") @PathVariable Long id) {
        clientUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
