package dev.yuri.addresses_api.dto.response;

public record EnderecoResponse(Long codigoEndereco, Long codigoPessoa, Long codigoBairro, String nomeRua, String numero,
    String complemento, String cep, DetailedBairroResponse bairro) { }
