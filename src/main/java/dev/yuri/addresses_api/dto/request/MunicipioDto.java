package dev.yuri.addresses_api.dto.request;

import jakarta.validation.constraints.*;

public record MunicipioDto(
    @NotNull(message = "{error.mandatory.codigo.uf}")
    @Min(value = 1, message = "{error.invalid.codigo.uf.length}")
    Long codigoUF,

    @NotBlank(message = "{error.mandatory.nome}")
    @Size(min = 1, max = 60, message = "{error.invalid.nome.length}")
    String nome,

    @NotNull(message = "{error.mandatory.status}")
    @Min(value = 1, message = "{error.invalid.status.value}")
    @Max(value = 2, message = "{error.invalid.status.value}")
    Integer status) {
}