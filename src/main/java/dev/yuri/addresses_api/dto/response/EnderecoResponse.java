package dev.yuri.addresses_api.dto.response;

import dev.yuri.addresses_api.entity.Endereco;

import java.util.List;

public record EnderecoResponse(
    Long codigoEndereco,
    Long codigoPessoa,
    Long codigoBairro,
    String nomeRua,
    String numero,
    String complemento,
    String cep,
    DetailedBairroResponse bairro
) {
    public static List<EnderecoResponse> fromEntities(List<Endereco> enderecos) {
        return enderecos.stream()
                .map(endereco -> new EnderecoResponse(
                        endereco.getCodigoEndereco(),
                        endereco.getPessoa().getCodigoPessoa(),
                        endereco.getBairro().getCodigoBairro(),
                        endereco.getNomeRua(),
                        endereco.getNumero(),
                        endereco.getComplemento(),
                        endereco.getCep(), new DetailedBairroResponse(
                                endereco.getBairro().getCodigoBairro(),
                                endereco.getBairro().getMunicipio().getCodigoMunicipio(),
                                endereco.getBairro().getNome(),
                                endereco.getBairro().getStatus(),
                                new DetailedMunicipioResponse(endereco.getBairro().getMunicipio())
                )))
                .toList();
    }
}
