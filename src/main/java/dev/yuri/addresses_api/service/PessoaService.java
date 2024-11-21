package dev.yuri.addresses_api.service;

import dev.yuri.addresses_api.controller.PessoaController;
import dev.yuri.addresses_api.dto.request.PessoaDto;
import dev.yuri.addresses_api.dto.request.PessoaUpdateDto;
import dev.yuri.addresses_api.entity.Endereco;
import dev.yuri.addresses_api.entity.Pessoa;
import dev.yuri.addresses_api.exception.EntityAlreadyExistsException;
import dev.yuri.addresses_api.exception.EntityNotFoundException;
import dev.yuri.addresses_api.repository.PessoaRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {
    private final PessoaRepository pessoaRepository;
    private final EnderecoService enderecoService;
    private final MessageSource messageSource;

    public PessoaService(PessoaRepository pessoaRepository,
                         EnderecoService enderecoService, MessageSource messageSource) {
        this.pessoaRepository = pessoaRepository;
        this.enderecoService = enderecoService;
        this.messageSource = messageSource;
    }

    public Pessoa save(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    public Pessoa save(PessoaDto pessoaDto) {
        this.assertUniqueness(pessoaDto.login());
        return this.save(new Pessoa(pessoaDto));
    }

    public Pessoa getByCodigoPessoa(Long codigoPessoa) {
        return pessoaRepository.findById(codigoPessoa)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.entity.not.exists",
                        new Object[]{"pessoa", codigoPessoa}, PessoaController.LOCALE_PT_BR)));
    }

    public Optional<Pessoa> findByLogin(String login) {
        return pessoaRepository.findByLogin(login);
    }

    public Optional<Pessoa> findElementByFilters(Long codigoPessoa, String login, Integer status) {
        return pessoaRepository.findElementByCodigoPessoaAndLoginAndStatus(codigoPessoa, login, status);
    }

    public List<Pessoa> getElementsByAppliedFilters(String login, Integer status) {
        return pessoaRepository.getElementsByLoginAndStatus(login, status);
    }

    public List<Pessoa> findAll() {
        return pessoaRepository.findAll();
    }

    public List<Endereco> getEnderecosByPessoa(Pessoa pessoa) {
        return enderecoService.getAllByPessoa(pessoa);
    }

    public void assertUniqueness(String login) {
        this.findByLogin(login)
                .ifPresent(pessoa -> {
                    throw new EntityAlreadyExistsException(messageSource.getMessage("error.pessoa.already.exists",
                            new Object[]{login}, PessoaController.LOCALE_PT_BR));
                });
    }

    public void assertUpdatable(Pessoa pessoa, PessoaUpdateDto pessoaUpdateDto) {
        if (hasSameAttributes(pessoa, pessoaUpdateDto)) {
            return;
        }

        this.assertUniqueness(pessoaUpdateDto.login());
    }

    private boolean hasSameAttributes(Pessoa pessoa, PessoaUpdateDto pessoaUpdateDto) {
        return pessoa.getLogin().equals(pessoaUpdateDto.login());
    }
}
