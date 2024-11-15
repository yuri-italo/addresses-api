package dev.yuri.addresses_api.mapper;

import dev.yuri.addresses_api.dto.response.PessoaResponse;
import dev.yuri.addresses_api.entity.Endereco;
import dev.yuri.addresses_api.entity.Pessoa;

import java.util.Collections;
import java.util.List;

public final class PessoaMapper {

    private PessoaMapper() {
        throw new UnsupportedOperationException("PessoaMapper não pode ser instanciada.");
    }

    public static PessoaResponse toResponse(Pessoa pessoa) {
        return new PessoaResponse(
                pessoa.getCodigoPessoa(),
                pessoa.getNome(),
                pessoa.getSobrenome(),
                pessoa.getIdade(),
                pessoa.getLogin(),
                pessoa.getSenha(),
                pessoa.getStatus(),
                Collections.emptyList()
        );
    }

    public static PessoaResponse toResponse(Pessoa pessoa, List<Endereco> enderecos) {
        return new PessoaResponse(
                pessoa.getCodigoPessoa(),
                pessoa.getNome(),
                pessoa.getSobrenome(),
                pessoa.getIdade(),
                pessoa.getLogin(),
                pessoa.getSenha(),
                pessoa.getStatus(),
                EnderecoMapper.toResponseList(enderecos)
        );
    }

    public static List<PessoaResponse> toResponseList(List<Pessoa> pessoas) {
        return pessoas.stream()
                .map(PessoaMapper::toResponse)
                .toList();
    }
}
