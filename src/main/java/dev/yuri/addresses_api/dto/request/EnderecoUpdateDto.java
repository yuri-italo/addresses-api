package dev.yuri.addresses_api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EnderecoUpdateDto(
    @Min(value = 1, message = "{error.invalid.codigo.endereco.length}")
    Long codigoEndereco,

    @NotNull(message = "{error.mandatory.codigo.pessoa}")
    @Min(value = 1, message = "{error.invalid.codigo.pessoa.length}")
    Long codigoPessoa,

    @NotNull(message = "{error.mandatory.codigo.bairro}")
    @Min(value = 1, message = "{error.invalid.codigo.bairro.length}")
    Long codigoBairro,

    @NotBlank(message = "{error.mandatory.nome.rua}")
    @Size(min = 1, max = 256, message = "{error.invalid.nome.rua.length}")
    String nomeRua,

    @NotBlank(message = "{error.mandatory.numero}")
    @Size(min = 1, max = 10, message = "{error.invalid.numero.length}")
    String numero,

    @Size(min = 1, max = 20, message = "{error.invalid.complemento.length}")
    String complemento,

    @NotBlank(message = "{error.mandatory.cep}")
    @Size(min = 9, max = 10, message = "{error.invalid.cep.length}")
    String cep) {
}
