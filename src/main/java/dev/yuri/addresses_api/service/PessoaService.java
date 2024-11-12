package dev.yuri.addresses_api.service;

import dev.yuri.addresses_api.controller.MunicipioController;
import dev.yuri.addresses_api.dto.request.PessoaDto;
import dev.yuri.addresses_api.dto.request.PessoaUpdateDto;
import dev.yuri.addresses_api.entity.Endereco;
import dev.yuri.addresses_api.entity.Pessoa;
import dev.yuri.addresses_api.exception.EntityAlreadyExistsException;
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

    public PessoaService(PessoaRepository pessoaRepository, EnderecoService enderecoService, MessageSource messageSource) {
        this.pessoaRepository = pessoaRepository;
        this.enderecoService = enderecoService;
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
        return enderecoService.findByPessoa(pessoa);
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

    public Pessoa save(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    public Optional<Pessoa> getByCodigoPessoa(Long codigoPessoa) {
        return pessoaRepository.findById(codigoPessoa);
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
