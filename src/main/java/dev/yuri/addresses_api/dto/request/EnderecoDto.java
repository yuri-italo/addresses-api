package dev.yuri.addresses_api.dto.request;

import jakarta.validation.constraints.*;

public record EnderecoDto(
    @NotNull(message = "{error.mandatory.codigo.bairro}")
    @Min(value = 1, message = "{error.invalid.codigo.bairro.length}")
    Long codigoBairro,

    @NotBlank(message = "{error.mandatory.nome.rua}")
    @Size(min = 1, max = 256, message = "{error.invalid.nome.rua.length}")
    String nomeRua,

    @NotBlank(message = "{error.mandatory.numero}")
    @Size(max = 10, message = "{error.invalid.numero.length}")
    @Pattern(regexp = "\\d+[A-Z]?", message = "{error.invalid.numero.format}")
    String numero,

    @Size(min = 1, max = 20, message = "{error.invalid.complemento.length}")
    String complemento,

    @NotBlank(message = "{error.mandatory.cep}")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "{error.invalid.cep.format}")
    String cep) {
}
