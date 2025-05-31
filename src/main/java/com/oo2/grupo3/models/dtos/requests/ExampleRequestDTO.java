package com.oo2.grupo3.models.dtos.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExampleRequestDTO {

    private Integer id;

    @NotBlank(message = "The name cannot be empty")
    private String name;

    @NotNull(message = "The price is mandatory")
    @DecimalMin(value = "0.01", message = "The price must be greater than 0")
    private BigDecimal price;
}