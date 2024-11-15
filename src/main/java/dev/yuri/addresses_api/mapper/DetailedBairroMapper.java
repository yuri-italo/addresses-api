package dev.yuri.addresses_api.mapper;

import dev.yuri.addresses_api.dto.response.DetailedBairroResponse;
import dev.yuri.addresses_api.entity.Bairro;
import dev.yuri.addresses_api.entity.Endereco;

public final class DetailedBairroMapper {

    private DetailedBairroMapper() {
        throw new UnsupportedOperationException("DetailedBairroMapper não pode ser instanciada.");
    }

    public static DetailedBairroResponse toResponseFromEndereco(Endereco endereco) {
        Bairro bairro = endereco.getBairro();
        return new DetailedBairroResponse(
                bairro.getCodigoBairro(),
                bairro.getMunicipio().getCodigoMunicipio(),
                bairro.getNome(),
                bairro.getStatus(),
                DetailedMunicipioMapper.toResponse(bairro.getMunicipio())
        );
    }

    public static DetailedBairroResponse toResponseFromBairro(Bairro bairro) {
        return new DetailedBairroResponse(
                bairro.getCodigoBairro(),
                bairro.getMunicipio().getCodigoMunicipio(),
                bairro.getNome(),
                bairro.getStatus(),
                DetailedMunicipioMapper.toResponse(bairro.getMunicipio())
        );
    }
}

