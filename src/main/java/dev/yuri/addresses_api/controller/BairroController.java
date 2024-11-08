package dev.yuri.addresses_api.controller;

import dev.yuri.addresses_api.dto.request.BairroDto;
import dev.yuri.addresses_api.dto.request.BairroUpdateDto;
import dev.yuri.addresses_api.dto.response.BairroResponse;
import dev.yuri.addresses_api.entity.Bairro;
import dev.yuri.addresses_api.exception.EntityNotFoundException;
import dev.yuri.addresses_api.exception.EntityNotSavedException;
import dev.yuri.addresses_api.exception.InvalidFilterException;
import dev.yuri.addresses_api.service.BairroService;
import dev.yuri.addresses_api.service.MunicipioService;
import dev.yuri.addresses_api.utils.ControllerUtil;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/bairro")
public class BairroController {
    private final BairroService bairroService;
    private final MunicipioService municipioService;
    private final MessageSource messageSource;
    private static final List<String> EXPECTED_FILTERS = Arrays.asList("codigoBairro", "codigoMunicipio", "nome", "status");
    public static final Locale LOCALE_PT_BR = new Locale("pt", "BR");

    public BairroController(BairroService bairroService, MunicipioService municipioService, MessageSource messageSource) {
        this.bairroService = bairroService;
        this.municipioService = municipioService;
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
        var invalidFilters = ControllerUtil.getInvalidFilters(EXPECTED_FILTERS, allFilters);
        if (!invalidFilters.isEmpty()) {
            throw new InvalidFilterException(
                    messageSource.getMessage("error.invalid.filters", new Object[]{invalidFilters}, LOCALE_PT_BR)
            );
        }

        if (ControllerUtil.isFiltersApplied(codigoBairro)) {
            Optional<Bairro> elementByFilters = bairroService.findElementByFilters(
                    codigoBairro, codigoMunicipio, nome, status);
            if (elementByFilters.isPresent()) {
                return ResponseEntity.ok(new BairroResponse(elementByFilters.get()));
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        }

        if (ControllerUtil.isFiltersApplied(codigoMunicipio, nome, status)) {
            var elementsByAppliedFilters = bairroService.findElementsByAppliedFilters(codigoMunicipio, nome, status);
            return ResponseEntity.ok(BairroResponse.fromEntities(elementsByAppliedFilters));
        }

        return ResponseEntity.ok(BairroResponse.fromEntities(bairroService.findAll()));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<List<BairroResponse>> save(@Valid @RequestBody BairroDto bairroDto) {
        var codigoMunicipio = bairroDto.codigoMunicipio();
        var municipio = municipioService.getByCodigoMunicipio(codigoMunicipio)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.entity.not.exists",
                        new Object[]{"município", codigoMunicipio}, LOCALE_PT_BR)));

        var bairro = new Bairro(bairroDto, municipio);
        bairroService.assertUniqueness(bairro.getMunicipio(), bairro.getNome());

        try {
            bairroService.save(bairro);
            return ResponseEntity.ok(BairroResponse.fromEntities(bairroService.findAll()));
        } catch (Exception e) {
            throw new EntityNotSavedException(
                    messageSource.getMessage("error.entity.not.saved", new Object[]{"bairro"}, LOCALE_PT_BR)
            );
        }
    }

    @PutMapping
    @Transactional
    public ResponseEntity<List<BairroResponse>> update(@Valid @RequestBody BairroUpdateDto bairroUpdateDto) {
        var codigoBairro = bairroUpdateDto.codigoBairro();
        var codigoMunicipio = bairroUpdateDto.codigoMunicipio();

        var bairro = bairroService.getByCodigoBairro(codigoBairro)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.entity.not.exists",
                        new Object[]{"bairro", codigoBairro}, LOCALE_PT_BR)));

        var municipio = municipioService.getByCodigoMunicipio(codigoMunicipio)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.entity.not.exists",
                        new Object[]{"município", codigoMunicipio}, LOCALE_PT_BR)));

        bairroService.assertUpdatable(bairro, bairroUpdateDto);

        try {
            BeanUtils.copyProperties(bairroUpdateDto, bairro);
            bairro.setMunicipio(municipio);
            bairroService.save(bairro);
            return ResponseEntity.ok(BairroResponse.fromEntities(bairroService.findAll()));
        } catch (Exception e) {
            throw new EntityNotSavedException(
                    messageSource.getMessage("error.entity.not.saved", new Object[]{"bairro"}, LOCALE_PT_BR)
            );
        }
    }
 }
