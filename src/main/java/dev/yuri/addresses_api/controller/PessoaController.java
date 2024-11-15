package dev.yuri.addresses_api.controller;

import dev.yuri.addresses_api.dto.request.PessoaDto;
import dev.yuri.addresses_api.dto.request.PessoaUpdateDto;
import dev.yuri.addresses_api.dto.response.PessoaResponse;
import dev.yuri.addresses_api.entity.Endereco;
import dev.yuri.addresses_api.entity.Pessoa;
import dev.yuri.addresses_api.exception.EntityNotSavedException;
import dev.yuri.addresses_api.service.EnderecoService;
import dev.yuri.addresses_api.service.PessoaService;
import dev.yuri.addresses_api.utils.ControllerUtil;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {
    private final PessoaService pessoaService;
    private final EnderecoService enderecoService;
    private final MessageSource messageSource;
    private static final List<String> EXPECTED_FILTERS = Arrays.asList("codigoPessoa", "login", "status");
    public static final Locale LOCALE_PT_BR = new Locale("pt", "BR");

    public PessoaController(PessoaService pessoaService, EnderecoService enderecoService, MessageSource messageSource) {
        this.pessoaService = pessoaService;
        this.enderecoService = enderecoService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ResponseEntity<?> findPessoa(
            @RequestParam(required = false) Long codigoPessoa,
            @RequestParam(required = false) String login,
            @RequestParam(required = false) Integer status,
            @RequestParam Map<String, String> allFilters
    ) {
        ControllerUtil.validateFilters(EXPECTED_FILTERS, allFilters, messageSource);

        if (ControllerUtil.isFiltersApplied(codigoPessoa)) {
            Optional<Pessoa> elementByFilters = pessoaService.findElementByFilters(codigoPessoa, login, status);
            if (elementByFilters.isPresent()) {
                var pessoa = elementByFilters.get();
                var enderecos = pessoaService.getEnderecosByPessoa(pessoa);
                return ResponseEntity.ok(new PessoaResponse(pessoa, enderecos));
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        }

        if (ControllerUtil.isFiltersApplied(login, status)) {
            var elementsByAppliedFilters = pessoaService.getElementsByAppliedFilters(login, status);
            return ResponseEntity.ok(PessoaResponse.fromEntities(elementsByAppliedFilters));
        }

        return ResponseEntity.ok(PessoaResponse.fromEntities(pessoaService.findAll()));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<List<PessoaResponse>> save(@Valid @RequestBody PessoaDto pessoaDto) {
        var pessoa = pessoaService.save(pessoaDto);
        var enderecos = enderecoService.toEntityList(pessoaDto, pessoa);

        try {
            enderecoService.saveAll(enderecos);
            return ResponseEntity.ok(PessoaResponse.fromEntities(pessoaService.findAll()));
        } catch (Exception e) {
            throw new EntityNotSavedException(
                    messageSource.getMessage("error.entity.not.saved", new Object[]{"pessoa"}, LOCALE_PT_BR)
            );
        }
    }

    @PutMapping
    @Transactional
    public ResponseEntity<List<PessoaResponse>> update(@Valid @RequestBody PessoaUpdateDto pessoaUpdateDto) {
        var pessoa = pessoaService.getByCodigoPessoa(pessoaUpdateDto.codigoPessoa());
        pessoaService.assertUpdatable(pessoa, pessoaUpdateDto);

        try {
            BeanUtils.copyProperties(pessoaUpdateDto, pessoa);
            pessoa = pessoaService.save(pessoa);
        } catch (Exception e) {
            throw new EntityNotSavedException(
                    messageSource.getMessage("error.entity.not.saved", new Object[]{"pessoa"}, LOCALE_PT_BR)
            );
        }

        List<Endereco> enderecoList = enderecoService.toEntityList(pessoaUpdateDto.enderecos());
        enderecoService.assertUpdatable(enderecoList, pessoa);

        try {
            enderecoService.saveAll(enderecoList, pessoa);
            return ResponseEntity.ok(PessoaResponse.fromEntities(pessoaService.findAll()));
        } catch (Exception e) {
            throw new EntityNotSavedException(
                    messageSource.getMessage("error.entity.not.saved", new Object[]{"endereco"}, LOCALE_PT_BR)
            );
        }
    }
}
