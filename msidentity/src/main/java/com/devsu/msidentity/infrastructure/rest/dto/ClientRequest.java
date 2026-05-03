package com.devsu.msidentity.infrastructure.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClientRequest {

    @NotBlank
    private String name;

    private String gender;

    @Min(15)
    private int age;

    @NotBlank
    private String identification;

    private String address;

    private String phone;

    private String password;

    @NotNull
    private Boolean status;
}
