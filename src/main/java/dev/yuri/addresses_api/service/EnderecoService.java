package dev.yuri.addresses_api.service;

import dev.yuri.addresses_api.dto.request.EnderecoUpdateDto;
import dev.yuri.addresses_api.dto.request.PessoaDto;
import dev.yuri.addresses_api.entity.Bairro;
import dev.yuri.addresses_api.entity.Endereco;
import dev.yuri.addresses_api.entity.Pessoa;
import dev.yuri.addresses_api.exception.AddressDoesNotBelongToPersonException;
import dev.yuri.addresses_api.exception.EntityNotFoundException;
import dev.yuri.addresses_api.repository.EnderecoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static dev.yuri.addresses_api.controller.PessoaController.LOCALE_PT_BR;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final BairroService bairroService;
    private final PessoaService pessoaService;
    private final MessageSource messageSource;

    public EnderecoService(EnderecoRepository enderecoRepository, BairroService bairroService, @Lazy PessoaService pessoaService, MessageSource messageSource) {
        this.enderecoRepository = enderecoRepository;
        this.bairroService = bairroService;
        this.pessoaService = pessoaService;
        this.messageSource = messageSource;
    }

    public void saveAll(List<Endereco> enderecos) {
        enderecoRepository.saveAll(enderecos);
    }

    public void saveAll(List<Endereco> enderecoList, Pessoa pessoa) {
        var addressesToDelete = enderecoRepository.findAllByPessoa(pessoa);

        enderecoList.forEach(endereco -> {
            if (endereco.getCodigoEndereco() == null) {
                enderecoRepository.save(endereco);
            } else {
                var codigoEndereco = endereco.getCodigoEndereco();
                var target = enderecoRepository.findById(codigoEndereco)
                        .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage(
                                "error.entity.not.exists", new Object[]{"endereço", codigoEndereco}, LOCALE_PT_BR)));

                BeanUtils.copyProperties(endereco, target);
                enderecoRepository.save(target);
                addressesToDelete.remove(endereco);
            }
        });
        addressesToDelete.forEach(enderecoRepository::delete);
    }

    public Optional<Endereco> getByCodigoEndereco(Long codigoEndereco) {
        return enderecoRepository.findById(codigoEndereco);
    }

    public List<Endereco> findByPessoa(Pessoa pessoa) {
        return enderecoRepository.findByPessoa(pessoa);
    }

    public List<Endereco> toEntityList(PessoaDto pessoaDto, Pessoa pessoa) {
        return pessoaDto.enderecos().stream().map(enderecoDto -> {
            Long codigoBairro = enderecoDto.codigoBairro();
            Bairro bairro = bairroService.getByCodigoBairro(codigoBairro);
            return new Endereco(pessoa, bairro, enderecoDto);
        }).toList();
    }

    public List<Endereco> toEntityList(List<EnderecoUpdateDto> enderecoUpdateDtos) {
        return enderecoUpdateDtos.stream().map(enderecoUpdateDto -> {
            var codigoEndereco = enderecoUpdateDto.codigoEndereco();
            var codigoBairro = enderecoUpdateDto.codigoBairro();
            var codigoPessoa = enderecoUpdateDto.codigoPessoa();

            if (codigoEndereco != null) {
                this.getByCodigoEndereco(codigoEndereco)
                    .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage(
                            "error.entity.not.exists", new Object[]{"endereço", codigoEndereco}, LOCALE_PT_BR)));
            }

            var bairro = bairroService.getByCodigoBairro(codigoBairro);

            var pessoa = pessoaService.getByCodigoPessoa(codigoPessoa);

            return new Endereco(enderecoUpdateDto, pessoa, bairro);
        }).collect(Collectors.toList());
    }

    public void assertUpdatable(List<Endereco> enderecos, Pessoa expectedOwner) {
        enderecos.stream()
                .filter(endereco -> !this.hasSameOwner(endereco.getPessoa(), expectedOwner))
                .findFirst()
                .ifPresent(endereco -> {
                    throw new AddressDoesNotBelongToPersonException(messageSource.getMessage(
                            "error.address.does.not.belong.to.person",
                            new Object[]{
                                endereco.getCodigoEndereco() != null ? endereco.getCodigoEndereco() : "não informado",
                                expectedOwner.getCodigoPessoa()
                            }, LOCALE_PT_BR));
                });

        List<Endereco> allByPessoa = enderecoRepository.findAllByPessoa(expectedOwner);

        enderecos.forEach(endereco -> {
            if (endereco.getCodigoEndereco() != null) {
                if (!allByPessoa.contains(endereco)) {
                    throw new AddressDoesNotBelongToPersonException(
                        messageSource.getMessage("error.address.does.not.belong.to.person",
                        new Object[]{endereco.getCodigoEndereco(), expectedOwner.getCodigoPessoa()}, LOCALE_PT_BR));
                }
            }
        });
    }

    private boolean hasSameOwner(Pessoa addressOwner, Pessoa expectedOwner) {
        return addressOwner.equals(expectedOwner);
    }
}
