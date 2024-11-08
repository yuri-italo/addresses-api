package dev.yuri.addresses_api.service;

import dev.yuri.addresses_api.dto.request.PessoaDto;
import dev.yuri.addresses_api.entity.Bairro;
import dev.yuri.addresses_api.entity.Endereco;
import dev.yuri.addresses_api.entity.Pessoa;
import dev.yuri.addresses_api.exception.EntityNotFoundException;
import dev.yuri.addresses_api.repository.EnderecoRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;

import static dev.yuri.addresses_api.controller.PessoaController.LOCALE_PT_BR;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final BairroService bairroService;
    private final MessageSource messageSource;

    public EnderecoService(EnderecoRepository enderecoRepository, BairroService bairroService, MessageSource messageSource) {
        this.enderecoRepository = enderecoRepository;
        this.bairroService = bairroService;
        this.messageSource = messageSource;
    }


    public void saveAll(List<Endereco> enderecos) {
        enderecoRepository.saveAll(enderecos);
    }

    public List<Endereco> toEntityList(PessoaDto pessoaDto, Pessoa pessoa) {
        return pessoaDto.enderecos().stream().map(enderecoDto -> {
            Long codigoBairro = enderecoDto.codigoBairro();
            Bairro bairro = bairroService.getByCodigoBairro(codigoBairro)
                    .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.entity.not.exists",
                                    new Object[]{"bairro", codigoBairro}, LOCALE_PT_BR)));

            return new Endereco(pessoa, bairro, enderecoDto);
        }).toList();
    }
}
