package dev.yuri.addresses_api.mapper;

import dev.yuri.addresses_api.dto.response.MunicipioResponse;
import dev.yuri.addresses_api.entity.Municipio;

import java.util.List;

public final class MunicipioMapper {

    private MunicipioMapper() {
        throw new UnsupportedOperationException("MunicipioMapper não pode ser instanciada.");
    }

    public static MunicipioResponse toResponse(Municipio municipio) {
        return new MunicipioResponse(
                municipio.getCodigoMunicipio(),
                municipio.getUf().getCodigoUF(),
                municipio.getNome(),
                municipio.getStatus()
        );
    }

    public static List<MunicipioResponse> toResponseList(List<Municipio> municipios) {
        return municipios.stream()
                .map(MunicipioMapper::toResponse)
                .toList();
    }
}
