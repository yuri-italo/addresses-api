package dev.yuri.addresses_api.dto.response;

public record BairroResponse(Long codigoBairro, Long codigoMunicipio, String nome, Integer status) { }
