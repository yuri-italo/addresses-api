package dev.yuri.addresses_api.mapper;

import dev.yuri.addresses_api.dto.response.BairroResponse;
import dev.yuri.addresses_api.entity.Bairro;

import java.util.List;

public final class BairroMapper {

    private BairroMapper() {
        throw new UnsupportedOperationException("BairroMapper não pode ser instanciada.");
    }

    public static BairroResponse toResponse(Bairro bairro) {
        return new BairroResponse(bairro.getCodigoBairro(),
                bairro.getMunicipio().getCodigoMunicipio(),
                bairro.getNome(),
                bairro.getStatus());
    }

    public static List<BairroResponse> toResponseList(List<Bairro> bairros) {
        return bairros.stream()
                .map(BairroMapper::toResponse)
                .toList();
    }
}

