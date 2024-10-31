package dev.yuri.addresses_api.controller;

import dev.yuri.addresses_api.dto.request.UFDto;
import dev.yuri.addresses_api.dto.request.UFUpdateDto;
import dev.yuri.addresses_api.entity.UF;
import dev.yuri.addresses_api.exception.EntityNotFoundException;
import dev.yuri.addresses_api.exception.EntityNotSavedException;
import dev.yuri.addresses_api.exception.InvalidFilterException;
import dev.yuri.addresses_api.service.UFService;
import dev.yuri.addresses_api.utils.ControllerUtils;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/uf")
public class UFController {
    private final UFService uFService;
    private final MessageSource messageSource;
    private static final List<String> EXPECTED_FILTERS = Arrays.asList("codigoUF", "sigla", "nome", "status");
    public static final Locale LOCALE_PT_BR = new Locale("pt", "BR");

    public UFController(UFService uFService, MessageSource messageSource) {
        this.uFService = uFService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ResponseEntity<?> findUF(
        @RequestParam(required = false) Long codigoUF,
        @RequestParam(required = false) String sigla,
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

        if (ControllerUtils.isUniqueResponse(codigoUF, sigla, nome)) {
            Optional<UF> elementByFilters = uFService.findElementByFilters(codigoUF, sigla, nome, status);
            if (elementByFilters.isPresent()) {
                return ResponseEntity.ok(elementByFilters.get());
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        }

        if (status != null) {
            return ResponseEntity.ok(uFService.findElementsByStatus(status));
        }

        return ResponseEntity.ok(uFService.findAll());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<List<UF>> save(@RequestBody @Valid UFDto uFDto) {
        try {
            uFService.save(new UF(uFDto));
            return ResponseEntity.ok(uFService.findAll());
        } catch (Exception e) {
            throw new EntityNotSavedException(
                messageSource.getMessage("error.post", new Object[]{"UF"}, LOCALE_PT_BR)
            );
        }
    }

    @PutMapping
    @Transactional
    public ResponseEntity<List<UF>> update(@RequestBody @Valid UFUpdateDto uFUpdateDto) {
        var codigo = uFUpdateDto.codigoUF();
        var uF = uFService.getByCodigoUF(codigo).orElseThrow(() -> new EntityNotFoundException(
            messageSource.getMessage("error.entity.not.exists", new Object[]{"UF", codigo}, LOCALE_PT_BR))
        );

        uFService.checkIfExists(uF, uFUpdateDto);

        try {
            BeanUtils.copyProperties(uFUpdateDto, uF);
            uFService.save(uF);
            return ResponseEntity.ok(uFService.findAll());
        } catch (Exception e) {
            throw new EntityNotSavedException(
                messageSource.getMessage("error.entity.not.saved", new Object[]{"UF"}, LOCALE_PT_BR)
            );
        }
    }
}
