package dev.yuri.addresses_api.controller;

import dev.yuri.addresses_api.dto.response.MunicipioResponse;
import dev.yuri.addresses_api.entity.Municipio;
import dev.yuri.addresses_api.exception.InvalidFilterException;
import dev.yuri.addresses_api.service.MunicipioService;
import dev.yuri.addresses_api.utils.ControllerUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/municipio")
public class MunicipioController {
    private final MunicipioService municipioService;
    private final MessageSource messageSource;
    private static final List<String> EXPECTED_FILTERS = Arrays.asList("codigoMunicipio","codigoUF", "nome", "status");
    public static final Locale LOCALE_PT_BR = new Locale("pt", "BR");

    public MunicipioController(MunicipioService municipioService, MessageSource messageSource) {
        this.municipioService = municipioService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ResponseEntity<?> findMunicipio(
            @RequestParam(required = false) Long codigoMunicipio,
            @RequestParam(required = false) Long codigoUF,
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

        if (ControllerUtils.isFiltersApplied(codigoMunicipio)) {
            Optional<Municipio> elementByFilters = municipioService.findElementByFilters(
                codigoMunicipio, codigoUF, nome, status);
            if (elementByFilters.isPresent()) {
                return ResponseEntity.ok(new MunicipioResponse(elementByFilters.get()));
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        }

        if (ControllerUtils.isFiltersApplied(codigoUF, nome, status)) {
            var elementsByAppliedFilters = municipioService.findElementsByAppliedFilters(codigoUF, nome, status);
            return ResponseEntity.ok(MunicipioResponse.fromEntities(elementsByAppliedFilters));
        }

        return ResponseEntity.ok(MunicipioResponse.fromEntities(municipioService.findAll()));
    }
}

