package com.devsu.msidentity.infrastructure.rest;

import com.devsu.msidentity.domain.port.in.ClientUseCase;
import com.devsu.msidentity.infrastructure.rest.dto.ClientRequest;
import com.devsu.msidentity.infrastructure.rest.dto.ClientResponse;
import com.devsu.msidentity.infrastructure.rest.mapper.ClientMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientUseCase clientUseCase;
    private final ClientMapper mapper;

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAll() {
        return ResponseEntity.ok(clientUseCase.getAll().stream()
                .map(mapper::toResponse)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(clientUseCase.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponse(clientUseCase.create(mapper.toDomain(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ClientRequest request) {
        return ResponseEntity.ok(mapper.toResponse(clientUseCase.update(id, mapper.toDomain(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        clientUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
