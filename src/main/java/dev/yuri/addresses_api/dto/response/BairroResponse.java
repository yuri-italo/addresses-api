package dev.yuri.addresses_api.dto.response;

import dev.yuri.addresses_api.entity.Bairro;

import java.util.List;

public record BairroResponse(Long codigoBairro, Long codigoMunicipio, String nome, Integer status) {
    public BairroResponse(Bairro bairro) {
        this(bairro.getCodigoBairro(),
                bairro.getMunicipio().getCodigoMunicipio(),
                bairro.getNome(),
                bairro.getStatus());
    }

    public static List<BairroResponse> fromEntities(List<Bairro> bairros) {
        return bairros.stream()
                .map(bairro -> new BairroResponse(
                        bairro.getCodigoBairro(),
                        bairro.getMunicipio().getCodigoMunicipio(),
                        bairro.getNome(),
                        bairro.getStatus()))
                .toList();
    }
}
