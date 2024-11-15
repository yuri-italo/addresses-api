package dev.yuri.addresses_api.mapper;

import dev.yuri.addresses_api.dto.response.DetailedMunicipioResponse;
import dev.yuri.addresses_api.entity.Municipio;

public final class DetailedMunicipioMapper {

    private DetailedMunicipioMapper() {
        throw new UnsupportedOperationException("DetailedMunicipioMapper não pode ser instanciada.");
    }

    public static DetailedMunicipioResponse toResponse(Municipio municipio) {
        return new DetailedMunicipioResponse(
                municipio.getCodigoMunicipio(),
                municipio.getUf().getCodigoUF(),
                municipio.getNome(),
                municipio.getStatus(),
                municipio.getUf()
        );
    }
}
