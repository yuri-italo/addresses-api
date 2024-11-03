package dev.yuri.addresses_api.dto.request;

import jakarta.validation.constraints.*;

public record BairroUpdateDto(
    @NotNull(message = "{error.mandatory.codigo.bairro}")
    @Min(value = 1, message = "{error.invalid.codigo.bairro.length}")
    Long codigoBairro,

    @NotNull(message = "{error.mandatory.codigo.municipio}")
    @Min(value = 1, message = "{error.invalid.codigo.municipio.length}")
    Long codigoMunicipio,

    @NotBlank(message = "{error.mandatory.nome}")
    @Size(min = 1, max = 60, message = "{error.invalid.nome.length}")
    String nome,

    @NotNull(message = "{error.mandatory.status}")
    @Min(value = 1, message = "{error.invalid.status.value}")
    @Max(value = 2, message = "{error.invalid.status.value}")
    Integer status) {
}
