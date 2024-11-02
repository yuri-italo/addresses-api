package dev.yuri.addresses_api.dto.response;

import dev.yuri.addresses_api.entity.Municipio;

import java.util.List;

public record MunicipioResponse(Long codigoMunicipio, Long codigoUF, String nome, Integer status) {
    public MunicipioResponse(Municipio municipio) {
        this(municipio.getCodigoMunicipio(),
                municipio.getUf().getCodigoUF(),
                municipio.getNome(),
                municipio.getStatus());
    }

    public static List<MunicipioResponse> fromEntities(List<Municipio> municipios) {
        return municipios.stream()
                .map(municipio -> new MunicipioResponse(
                        municipio.getCodigoMunicipio(),
                        municipio.getUf().getCodigoUF(),
                        municipio.getNome(),
                        municipio.getStatus()))
                .toList();
    }
}
