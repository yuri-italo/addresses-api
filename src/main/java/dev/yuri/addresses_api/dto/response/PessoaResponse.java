package dev.yuri.addresses_api.dto.response;

import dev.yuri.addresses_api.entity.Endereco;
import dev.yuri.addresses_api.entity.Pessoa;

import java.util.Collections;
import java.util.List;

public record PessoaResponse(Long codigoPessoa, String nome, String sobrenome, Integer idade, String login,
                             String senha, Integer status, List<EnderecoResponse> enderecos) {
    public PessoaResponse(Pessoa pessoa) {
        this(pessoa.getCodigoPessoa(),
                pessoa.getNome(),
                pessoa.getSobrenome(),
                pessoa.getIdade(),
                pessoa.getLogin(),
                pessoa.getSenha(),
                pessoa.getStatus(),
                Collections.emptyList());
    }

    public PessoaResponse(Pessoa pessoa, List<Endereco> enderecos) {
        this(pessoa.getCodigoPessoa(),
                pessoa.getNome(),
                pessoa.getSobrenome(),
                pessoa.getIdade(),
                pessoa.getLogin(),
                pessoa.getSenha(),
                pessoa.getStatus(),
                EnderecoResponse.fromEntities(enderecos));
    }

    public static List<PessoaResponse> fromEntities(List<Pessoa> pessoas) {
        return pessoas.stream()
                .map(pessoa -> new PessoaResponse(
                        pessoa.getCodigoPessoa(),
                        pessoa.getNome(),
                        pessoa.getSobrenome(),
                        pessoa.getIdade(),
                        pessoa.getLogin(),
                        pessoa.getSenha(),
                        pessoa.getStatus(),
                        Collections.emptyList()))
                .toList();
    }
}
