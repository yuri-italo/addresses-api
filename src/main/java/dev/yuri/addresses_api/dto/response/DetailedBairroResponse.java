package dev.yuri.addresses_api.dto.response;

public record DetailedBairroResponse(
        Long codigoBairro, Long codigoMunicipio, String nome, Integer status, DetailedMunicipioResponse municipio
) {}
