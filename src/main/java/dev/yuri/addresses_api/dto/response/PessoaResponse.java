package dev.yuri.addresses_api.dto.response;

import java.util.List;

public record PessoaResponse(Long codigoPessoa, String nome, String sobrenome, Integer idade, String login,
                             String senha, Integer status, List<EnderecoResponse> enderecos) { }
