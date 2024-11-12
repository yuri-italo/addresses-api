package dev.yuri.addresses_api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record PessoaUpdateDto(
    @NotNull(message = "{error.mandatory.codigo.pessoa}")
    @Min(value = 1, message = "{error.invalid.codigo.pessoa.length}")
    Long codigoPessoa,

    @NotBlank(message = "{error.mandatory.nome}")
    @Size(min = 1, max = 256, message = "{error.invalid.pessoa.nome.length}")
    String nome,

    @NotBlank(message = "{error.mandatory.sobrenome}")
    @Size(min = 1, max = 256, message = "{error.invalid.sobrenome.length}")
    String sobrenome,

    @NotNull(message = "{error.mandatory.idade}")
    @Min(value = 1, message = "{error.invalid.idade.value}")
    @Max(value = 150, message = "{error.invalid.idade.value}")
    Integer idade,

    @NotBlank(message = "{error.mandatory.login}")
    @Size(min = 4, max = 50, message = "{error.invalid.login.length}")
    String login,

    @NotBlank(message = "{error.mandatory.senha}")
    @Size(min = 8, max = 50, message = "{error.invalid.senha.length}")
    String senha,

    @NotNull(message = "{error.mandatory.status}")
    @Min(value = 1, message = "{error.invalid.status.value}")
    @Max(value = 2, message = "{error.invalid.status.value}")
    Integer status,

    @Valid
    @NotNull(message = "{error.mandatory.enderecos}")
    List<EnderecoUpdateDto> enderecos) {
}
