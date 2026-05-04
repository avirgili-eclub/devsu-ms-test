package com.devsu.msfinance.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "known_client")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KnownClient {

    @Id
    @Column(name = "client_id")
    private Long clientId;
}
