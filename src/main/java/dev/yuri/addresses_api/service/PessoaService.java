package dev.yuri.addresses_api.service;

import dev.yuri.addresses_api.entity.Endereco;
import dev.yuri.addresses_api.entity.Pessoa;
import dev.yuri.addresses_api.repository.EnderecoRepository;
import dev.yuri.addresses_api.repository.PessoaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {
    private final PessoaRepository pessoaRepository;
    private final EnderecoRepository enderecoRepository;

    public PessoaService(PessoaRepository pessoaRepository, EnderecoRepository enderecoRepository) {
        this.pessoaRepository = pessoaRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public Optional<Pessoa> findElementByFilters(Long codigoPessoa, String login, Integer status) {
        return pessoaRepository.getElementByFilters(codigoPessoa, login, status);
    }

    public List<Pessoa> findElementsByAppliedFilters(String login, Integer status) {
        return pessoaRepository.getElementsByAppliedFields(login, status);
    }

    public List<Pessoa> findAll() {
        return pessoaRepository.findAll();
    }

    public List<Endereco> getEnderecosByPessoa(Pessoa pessoa) {
        return enderecoRepository.findByPessoa(pessoa);
    }
}
