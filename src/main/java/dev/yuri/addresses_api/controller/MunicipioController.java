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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
        summary = "Buscar entidades de Município",
        description = "Busca entidades de Município com base em filtros opcionais, como código do município, código UF, nome e status.",
        parameters = {
            @Parameter(name = "codigoMunicipio",
                    description = "O identificador único do município. Exemplo: 123",
                    schema = @Schema(type = "long", example = "50", description = "Exemplo de código Município")),
            @Parameter(name = "codigoUF",
                    description = "O identificador único da UF associada. Exemplo: 123",
                    schema = @Schema(type = "long", example = "25", description = "Exemplo de código UF")),
            @Parameter(name = "nome",
                    description = "O nome do município. Exemplo: Campinas",
                    schema = @Schema(type = "string", example = "Campinas", description = "Exemplo de nome")),
            @Parameter(name = "status",
                    description = "O status do município. Exemplo: 1 para ativo, 2 para inativo.",
                    schema = @Schema(type = "integer", example = "1", description = "Exemplo de status"))
        },
        responses = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de municípios ou uma única entidade que corresponde aos filtros.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400",
                    description = "Filtros inválidos ou mal formatados.",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500",
                    description = "Erro interno no servidor.",
                    content = @Content(mediaType = "application/json"))
        }
    )
    public ResponseEntity<?> findMunicipio(
            @RequestParam(required = false) Long codigoMunicipio,
            @RequestParam(required = false) Long codigoUF,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Map<String, String> allFilters
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
    @Operation(
        summary = "Salvar uma nova entidade de Município",
        description = "Cria uma nova entidade de Município com os dados fornecidos.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Objeto MunicipioDto contendo os detalhes do município.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MunicipioDto.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200",
                description = "Entidade de Município salva com sucesso.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400",
                description = "Corpo da requisição inválido.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404",
                description = "Código de UF não encontrado.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409",
                description = "Dados conflitantes ou recurso já existente.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500",
                description = "Erro interno no servidor.",
                content = @Content(mediaType = "application/json"))
        }
    )
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
    @Operation(
        summary = "Atualizar uma entidade de Município existente",
        description = "Atualiza os dados de uma entidade de Município existente com base no MunicipioUpdateDto fornecido.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Objeto MunicipioUpdateDto contendo os detalhes atualizados.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MunicipioUpdateDto.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200",
                description = "Entidade de Município atualizada com sucesso.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400",
                description = "Corpo da requisição inválido.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404",
                description = "Entidade Município ou UF não encontrada.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409",
                description = "Dados conflitantes ou recurso já existente.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500",
                description = "Erro interno no servidor.",
                content = @Content(mediaType = "application/json"))
        }
    )
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

