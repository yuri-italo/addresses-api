package dev.yuri.addresses_api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record PessoaDto(
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
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])[A-Za-z\\d@#$%^&+=]{8,50}$",
            message = "{error.invalid.senha.format}")
    String senha,

    @NotNull(message = "{error.mandatory.status}")
    @Min(value = 1, message = "{error.invalid.status.value}")
    @Max(value = 2, message = "{error.invalid.status.value}")
    Integer status,

    @Valid
    @NotNull(message = "{error.mandatory.enderecos}")
    @NotEmpty(message = "{error.at.least.one.address.required}")
    List<EnderecoDto> enderecos) {
}
