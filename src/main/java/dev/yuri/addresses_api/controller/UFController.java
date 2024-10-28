package dev.yuri.addresses_api.controller;

import dev.yuri.addresses_api.exception.EntityNotFoundException;
import dev.yuri.addresses_api.exception.InvalidParamException;
import dev.yuri.addresses_api.service.UFService;
import dev.yuri.addresses_api.utils.ControllerUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/uf")
public class UFController {
    private final UFService uFService;
    private final MessageSource messageSource;
    private static final List<String> EXPECTED_PARAMS = Arrays.asList("codigoUF", "sigla", "nome", "status");

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
        @RequestParam Map<String, String> allParams
    ) {
        if (!ControllerUtils.isValidParams(EXPECTED_PARAMS, allParams)) {
            throw new InvalidParamException(messageSource.getMessage("get.error", new Object[]{"UF"},
                new Locale("pt", "BR")
            ));
        }

        if (ControllerUtils.isUniqueResponse(codigoUF, sigla, nome)) {
            return uFService.findElementByFilters(codigoUF, sigla, nome, status)
                .map(ResponseEntity::ok)
                .orElseThrow(EntityNotFoundException::new);
        } else if (status != null) {
            return ResponseEntity.ok(uFService.findElementsByStatus(status));
        } else {
            return ResponseEntity.ok(uFService.findAll());
        }
    }
}
