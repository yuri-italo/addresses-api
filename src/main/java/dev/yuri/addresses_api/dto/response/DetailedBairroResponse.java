package dev.yuri.addresses_api.dto.response;

import dev.yuri.addresses_api.entity.Endereco;

public record DetailedBairroResponse(
    Long codigoBairro,
    Long codigoMunicipio,
    String nome,
    Integer status,
    DetailedMunicipioResponse municipio
) {
    public DetailedBairroResponse(Endereco endereco) {
        this(endereco.getBairro().getCodigoBairro(),
            endereco.getBairro().getMunicipio().getCodigoMunicipio(),
            endereco.getBairro().getNome(),
            endereco.getBairro().getStatus(),
            new DetailedMunicipioResponse(endereco.getBairro().getMunicipio())
        );
    }
}
