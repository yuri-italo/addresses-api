package dev.yuri.addresses_api.dto.response;

import dev.yuri.addresses_api.entity.Municipio;
import dev.yuri.addresses_api.entity.UF;

public record DetailedMunicipioResponse(
    Long codigoMunicipio,
    Long codigoUF,
    String nome,
    Integer status,
    UF uf
) {
    public DetailedMunicipioResponse(Municipio municipio) {
        this(municipio.getCodigoMunicipio(),
                municipio.getUf().getCodigoUF(),
                municipio.getNome(),
                municipio.getStatus(),
                municipio.getUf());
    }
}
