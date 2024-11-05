package dev.yuri.addresses_api.controller;

import dev.yuri.addresses_api.dto.response.PessoaResponse;
import dev.yuri.addresses_api.entity.Pessoa;
import dev.yuri.addresses_api.exception.InvalidFilterException;
import dev.yuri.addresses_api.service.PessoaService;
import dev.yuri.addresses_api.utils.ControllerUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {
    private final PessoaService pessoaService;
    private final MessageSource messageSource;
    private static final List<String> EXPECTED_FILTERS = Arrays.asList("codigoPessoa", "login", "status");
    public static final Locale LOCALE_PT_BR = new Locale("pt", "BR");

    public PessoaController(PessoaService pessoaService, MessageSource messageSource) {
        this.pessoaService = pessoaService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ResponseEntity<?> findPessoa(
            @RequestParam(required = false) Long codigoPessoa,
            @RequestParam(required = false) String login,
            @RequestParam(required = false) Integer status,
            @RequestParam Map<String, String> allFilters
    ) {
        var invalidFilters = ControllerUtils.getInvalidFilters(EXPECTED_FILTERS, allFilters);
        if (!invalidFilters.isEmpty()) {
            throw new InvalidFilterException(
                    messageSource.getMessage("error.invalid.filters", new Object[]{invalidFilters}, LOCALE_PT_BR)
            );
        }

        if (ControllerUtils.isFiltersApplied(codigoPessoa)) {
            Optional<Pessoa> elementByFilters = pessoaService.findElementByFilters(codigoPessoa, login, status);
            if (elementByFilters.isPresent()) {
                var pessoa = elementByFilters.get();
                var enderecos = pessoaService.getEnderecosByPessoa(pessoa);
                return ResponseEntity.ok(new PessoaResponse(pessoa, enderecos));
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        }

        if (ControllerUtils.isFiltersApplied(login, status)) {
            var elementsByAppliedFilters = pessoaService.findElementsByAppliedFilters(login, status);
            return ResponseEntity.ok(PessoaResponse.fromEntities(elementsByAppliedFilters));
        }

        return ResponseEntity.ok(PessoaResponse.fromEntities(pessoaService.findAll()));
    }
}
