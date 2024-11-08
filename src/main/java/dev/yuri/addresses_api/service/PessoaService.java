package dev.yuri.addresses_api.service;

import dev.yuri.addresses_api.controller.MunicipioController;
import dev.yuri.addresses_api.dto.request.PessoaDto;
import dev.yuri.addresses_api.entity.Endereco;
import dev.yuri.addresses_api.entity.Pessoa;
import dev.yuri.addresses_api.exception.EntityAlreadyExistsException;
import dev.yuri.addresses_api.repository.EnderecoRepository;
import dev.yuri.addresses_api.repository.PessoaRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {
    private final PessoaRepository pessoaRepository;
    private final EnderecoRepository enderecoRepository;
    private final MessageSource messageSource;

    public PessoaService(PessoaRepository pessoaRepository, EnderecoRepository enderecoRepository, MessageSource messageSource) {
        this.pessoaRepository = pessoaRepository;
        this.enderecoRepository = enderecoRepository;
        this.messageSource = messageSource;
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

    public void assertUniqueness(String login) {
        var optionalPessoa = pessoaRepository.findByLogin(login);

        if (optionalPessoa.isPresent()) {
            throw new EntityAlreadyExistsException(
                    messageSource.getMessage("error.entity.already.exists",
                            new Object[]{"pessoa", "login", login}, MunicipioController.LOCALE_PT_BR)
            );
        }
    }

    public Pessoa save(PessoaDto pessoaDto) {
        this.assertUniqueness(pessoaDto.login());
        return pessoaRepository.save(new Pessoa(pessoaDto));
    }
}
