package dev.yuri.addresses_api.controller;

import dev.yuri.addresses_api.dto.request.MunicipioDto;
import dev.yuri.addresses_api.dto.request.MunicipioUpdateDto;
import dev.yuri.addresses_api.dto.response.MunicipioResponse;
import dev.yuri.addresses_api.entity.Municipio;
import dev.yuri.addresses_api.exception.EntityNotFoundException;
import dev.yuri.addresses_api.exception.EntityNotSavedException;
import dev.yuri.addresses_api.exception.InvalidFilterException;
import dev.yuri.addresses_api.service.MunicipioService;
import dev.yuri.addresses_api.service.UFService;
import dev.yuri.addresses_api.utils.ControllerUtil;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/municipio")
public class MunicipioController {
    private final MunicipioService municipioService;
    private final UFService uFService;
    private final MessageSource messageSource;
    private static final List<String> EXPECTED_FILTERS = Arrays.asList("codigoMunicipio","codigoUF", "nome", "status");
    public static final Locale LOCALE_PT_BR = new Locale("pt", "BR");

    public MunicipioController(MunicipioService municipioService, UFService uFService, MessageSource messageSource) {
        this.municipioService = municipioService;
        this.uFService = uFService;
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
        var invalidFilters = ControllerUtil.getInvalidFilters(EXPECTED_FILTERS, allFilters);
        if (!invalidFilters.isEmpty()) {
            throw new InvalidFilterException(
                    messageSource.getMessage("error.invalid.filters", new Object[]{invalidFilters}, LOCALE_PT_BR)
            );
        }

        if (ControllerUtil.isFiltersApplied(codigoMunicipio)) {
            Optional<Municipio> elementByFilters = municipioService.findElementByFilters(
                codigoMunicipio, codigoUF, nome, status);
            if (elementByFilters.isPresent()) {
                return ResponseEntity.ok(new MunicipioResponse(elementByFilters.get()));
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        }

        if (ControllerUtil.isFiltersApplied(codigoUF, nome, status)) {
            var elementsByAppliedFilters = municipioService.findElementsByAppliedFilters(codigoUF, nome, status);
            return ResponseEntity.ok(MunicipioResponse.fromEntities(elementsByAppliedFilters));
        }

        return ResponseEntity.ok(MunicipioResponse.fromEntities(municipioService.findAll()));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<List<MunicipioResponse>> save(@Valid @RequestBody MunicipioDto municipioDto) {
        var codigoUF = municipioDto.codigoUF();
        var uF = uFService.getByCodigoUF(codigoUF).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("error.entity.not.exists", new Object[]{"UF", codigoUF}, LOCALE_PT_BR)));

        var municipio = new Municipio(municipioDto, uF);
        municipioService.assertUniqueness(municipio.getUf(), municipio.getNome());

        try {
            municipioService.save(municipio);
            return ResponseEntity.ok(MunicipioResponse.fromEntities(municipioService.findAll()));
        } catch (Exception e) {
            throw new EntityNotSavedException(
                    messageSource.getMessage("error.entity.not.saved", new Object[]{"município"}, LOCALE_PT_BR)
            );
        }
    }

    @PutMapping
    @Transactional
    public ResponseEntity<List<MunicipioResponse>> update(@Valid @RequestBody MunicipioUpdateDto municipioUpdateDto) {
        var codigoMunicipio = municipioUpdateDto.codigoMunicipio();
        var codigoUF = municipioUpdateDto.codigoUF();

        var municipio = municipioService.getByCodigoMunicipio(codigoMunicipio)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.entity.not.exists",
                        new Object[]{"município", codigoMunicipio}, LOCALE_PT_BR)));

        var uF = uFService.getByCodigoUF(codigoUF)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.entity.not.exists",
                        new Object[]{"UF", codigoUF}, LOCALE_PT_BR)));

        municipioService.assertUpdatable(municipio, municipioUpdateDto);

        try {
            BeanUtils.copyProperties(municipioUpdateDto, municipio);
            municipio.setUf(uF);
            municipioService.save(municipio);
            return ResponseEntity.ok(MunicipioResponse.fromEntities(municipioService.findAll()));
        } catch (Exception e) {
            throw new EntityNotSavedException(
                    messageSource.getMessage("error.entity.not.saved", new Object[]{"município"}, LOCALE_PT_BR)
            );
        }

    }
}

