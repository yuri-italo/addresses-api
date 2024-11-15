package dev.yuri.addresses_api.mapper;

import dev.yuri.addresses_api.dto.response.DetailedBairroResponse;
import dev.yuri.addresses_api.dto.response.EnderecoResponse;
import dev.yuri.addresses_api.entity.Endereco;

import java.util.List;

public final class EnderecoMapper {

    private EnderecoMapper() {
        throw new UnsupportedOperationException("EnderecoMapper não pode ser instanciada.");
    }

    public static EnderecoResponse toResponse(Endereco endereco) {
        return new EnderecoResponse(
                endereco.getCodigoEndereco(),
                endereco.getPessoa().getCodigoPessoa(),
                endereco.getBairro().getCodigoBairro(),
                endereco.getNomeRua(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getCep(),
                new DetailedBairroResponse(
                        endereco.getBairro().getCodigoBairro(),
                        endereco.getBairro().getMunicipio().getCodigoMunicipio(),
                        endereco.getBairro().getNome(),
                        endereco.getBairro().getStatus(),
                        DetailedMunicipioMapper.toResponse(endereco.getBairro().getMunicipio())
                )
        );
    }

    public static List<EnderecoResponse> toResponseList(List<Endereco> enderecos) {
        return enderecos.stream()
                .map(EnderecoMapper::toResponse)
                .toList();
    }
}

