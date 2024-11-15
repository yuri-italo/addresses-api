package dev.yuri.addresses_api.controller;

import dev.yuri.addresses_api.dto.request.MunicipioDto;
import dev.yuri.addresses_api.dto.request.MunicipioUpdateDto;
import dev.yuri.addresses_api.dto.response.MunicipioResponse;
import dev.yuri.addresses_api.entity.Municipio;
import dev.yuri.addresses_api.exception.EntityNotSavedException;
import dev.yuri.addresses_api.mapper.MunicipioMapper;
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
        ControllerUtil.validateFilters(EXPECTED_FILTERS, allFilters, messageSource);

        if (ControllerUtil.isFiltersApplied(codigoMunicipio)) {
            Optional<Municipio> elementByFilters = municipioService.findElementByFilters(codigoMunicipio, codigoUF, nome, status);
            if (elementByFilters.isPresent()) {
                return ResponseEntity.ok(MunicipioMapper.toResponse(elementByFilters.get()));
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        }

        if (ControllerUtil.isFiltersApplied(codigoUF, nome, status)) {
            var elementsByAppliedFilters = municipioService.getElementsByAppliedFilters(codigoUF, nome, status);
            return ResponseEntity.ok(MunicipioMapper.toResponseList((elementsByAppliedFilters)));
        }

        return ResponseEntity.ok(MunicipioMapper.toResponseList(municipioService.findAll()));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<List<MunicipioResponse>> save(@Valid @RequestBody MunicipioDto municipioDto) {
        var municipio = new Municipio(municipioDto, uFService.getByCodigoUF(municipioDto.codigoUF()));
        municipioService.assertUniqueness(municipio.getUf(), municipio.getNome());

        try {
            municipioService.save(municipio);
            return ResponseEntity.ok(MunicipioMapper.toResponseList(municipioService.findAll()));
        } catch (Exception e) {
            throw new EntityNotSavedException(
                    messageSource.getMessage("error.entity.not.saved", new Object[]{"município"}, LOCALE_PT_BR)
            );
        }
    }

    @PutMapping
    @Transactional
    public ResponseEntity<List<MunicipioResponse>> update(@Valid @RequestBody MunicipioUpdateDto municipioUpdateDto) {
        var municipio = municipioService.getByCodigoMunicipio(municipioUpdateDto.codigoMunicipio());
        var uF = uFService.getByCodigoUF(municipioUpdateDto.codigoUF());
        municipioService.assertUpdatable(municipio, municipioUpdateDto);

        try {
            BeanUtils.copyProperties(municipioUpdateDto, municipio);
            municipio.setUf(uF);
            municipioService.save(municipio);
            return ResponseEntity.ok(MunicipioMapper.toResponseList(municipioService.findAll()));
        } catch (Exception e) {
            throw new EntityNotSavedException(
                    messageSource.getMessage("error.entity.not.saved", new Object[]{"município"}, LOCALE_PT_BR)
            );
        }
    }
}

