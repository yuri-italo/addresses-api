package dev.yuri.addresses_api.dto.response;

import dev.yuri.addresses_api.entity.UF;

public record DetailedMunicipioResponse(Long codigoMunicipio, Long codigoUF, String nome, Integer status, UF uf) { }
