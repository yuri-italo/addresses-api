package dev.yuri.addresses_api.controller;

import dev.yuri.addresses_api.dto.response.BairroResponse;
import dev.yuri.addresses_api.entity.Bairro;
import dev.yuri.addresses_api.exception.InvalidFilterException;
import dev.yuri.addresses_api.service.BairroService;
import dev.yuri.addresses_api.utils.ControllerUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/bairro")
public class BairroController {
    private final BairroService bairroService;
    private final MessageSource messageSource;
    private static final List<String> EXPECTED_FILTERS = Arrays.asList("codigoBairro", "codigoMunicipio", "nome", "status");
    public static final Locale LOCALE_PT_BR = new Locale("pt", "BR");

    public BairroController(BairroService bairroService, MessageSource messageSource) {
        this.bairroService = bairroService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ResponseEntity<?> findBairro(
            @RequestParam(required = false) Long codigoBairro,
            @RequestParam(required = false) Long codigoMunicipio,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer status,
            @RequestParam Map<String, String> allFilters
    ) {
        var invalidFilters = ControllerUtils.getInvalidFilters(EXPECTED_FILTERS, allFilters);
        if (!invalidFilters.isEmpty()) {
            throw new InvalidFilterException(
                    messageSource.getMessage("error.invalid.filters", new Object[]{invalidFilters}, LOCALE_PT_BR)
            );
        }

        if (ControllerUtils.isFiltersApplied(codigoBairro)) {
            Optional<Bairro> elementByFilters = bairroService.findElementByFilters(
                    codigoBairro, codigoMunicipio, nome, status);
            if (elementByFilters.isPresent()) {
                return ResponseEntity.ok(new BairroResponse(elementByFilters.get()));
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        }

        if (ControllerUtils.isFiltersApplied(codigoMunicipio, nome, status)) {
            var elementsByAppliedFilters = bairroService.findElementsByAppliedFilters(codigoMunicipio, nome, status);
            return ResponseEntity.ok(BairroResponse.fromEntities(elementsByAppliedFilters));
        }

        return ResponseEntity.ok(BairroResponse.fromEntities(bairroService.findAll()));
    }
}
