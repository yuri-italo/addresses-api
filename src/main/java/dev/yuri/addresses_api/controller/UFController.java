package dev.yuri.addresses_api.controller;

import dev.yuri.addresses_api.dto.request.UFDto;
import dev.yuri.addresses_api.dto.request.UFUpdateDto;
import dev.yuri.addresses_api.dto.response.ErrorResponse;
import dev.yuri.addresses_api.entity.UF;
import dev.yuri.addresses_api.exception.EntityNotSavedException;
import dev.yuri.addresses_api.service.UFService;
import dev.yuri.addresses_api.utils.ControllerUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/uf")
@Tag(name = "UF",
    description = "Gerenciamento das entidades de UF, incluindo operações de criação, consulta e atualização.")
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
    @Operation(
        summary = "Buscar entidades de UF",
        description = "Busca por entidades de UF com base em filtros opcionais como codigoUF, sigla, nome e status.",
        parameters = {
            @Parameter(name = "codigoUF", description = "O identificador único da UF.", example = "25"),
            @Parameter(name = "sigla", description = "A sigla da UF.", example = "SP"),
            @Parameter(name = "nome", description = "O nome da UF.", example = "São Paulo"),
            @Parameter(name = "status", description = "O status da UF. 1 para ativo, 2 para inativo", example = "1")
        },
        responses = {
            @ApiResponse(responseCode = "200",
                description = "Lista de UF ou uma única entidade que corresponde aos filtros.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UF.class))),
            @ApiResponse(responseCode = "400",
                description = "Filtros inválidos ou mal formatados.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                description = "Erro interno no servidor",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<?> findUF(
        @RequestParam(required = false) Long codigoUF,
        @RequestParam(required = false) String sigla,
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) Integer status,
        @RequestParam(required = false) Map<String, String> allFilters
    ) {
        ControllerUtil.validateFilters(EXPECTED_FILTERS, allFilters, messageSource);

        if (ControllerUtil.isFiltersApplied(codigoUF, sigla, nome)) {
            Optional<UF> elementByFilters = uFService.findElementByFilters(codigoUF, sigla, nome, status);
            if (elementByFilters.isPresent()) {
                return ResponseEntity.ok(elementByFilters.get());
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        }

        if (status != null) {
            return ResponseEntity.ok(uFService.getElementsByStatus(status));
        }

        return ResponseEntity.ok(uFService.findAll());
    }

    @PostMapping
    @Transactional
    @Operation(
        summary = "Salvar uma nova entidade de UF",
        description = "Cria uma nova entidade de UF com os detalhes fornecidos.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Objeto UFDto contendo os detalhes da entidade UF",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UFDto.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200",
                description = "Entidade de UF salva com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UF.class))),
            @ApiResponse(responseCode = "400",
                description = "Corpo da requisição inválido",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409",
                description = "Dados conflitantes ou recurso já existente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                description = "Erro interno no servidor",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<List<UF>> save(@RequestBody @Valid UFDto uFDto) {
        var uf = new UF(uFDto);
        uFService.assertUniqueness(uf);

        try {
            uFService.save(uf);
            return ResponseEntity.ok(uFService.findAll());
        } catch (Exception e) {
            throw new EntityNotSavedException(
                messageSource.getMessage("error.entity.not.saved", new Object[]{"UF"}, LOCALE_PT_BR)
            );
        }
    }

    @PutMapping
    @Transactional
    @Operation(
        summary = "Atualizar uma entidade de UF existente",
        description = "Atualiza os detalhes de uma entidade UF existente com base no UFUpdateDto fornecido.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Objeto UFUpdateDto contendo os detalhes atualizados",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UFUpdateDto.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200",
                description = "Entidade de UF atualizada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UF.class))),
            @ApiResponse(responseCode = "400",
                description = "Corpo da requisição inválido",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409",
                description = "Dados conflitantes ou recurso já existente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                description = "Erro interno no servidor",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<List<UF>> update(@RequestBody @Valid UFUpdateDto uFUpdateDto) {
        var uF = uFService.getByCodigoUF(uFUpdateDto.codigoUF());
        uFService.assertUpdatable(uF, uFUpdateDto);

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
