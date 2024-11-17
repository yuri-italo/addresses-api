package dev.yuri.addresses_api.controller;

import dev.yuri.addresses_api.dto.request.BairroDto;
import dev.yuri.addresses_api.dto.request.BairroUpdateDto;
import dev.yuri.addresses_api.dto.response.BairroResponse;
import dev.yuri.addresses_api.dto.response.ErrorResponse;
import dev.yuri.addresses_api.entity.Bairro;
import dev.yuri.addresses_api.exception.EntityNotSavedException;
import dev.yuri.addresses_api.mapper.BairroMapper;
import dev.yuri.addresses_api.service.BairroService;
import dev.yuri.addresses_api.service.MunicipioService;
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
@RequestMapping("/bairro")
@Tag(name = "Bairro",
    description = "Gerenciamento das entidades de bairros, incluindo operações de criação, consulta e atualização.")
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
    @Operation(
        summary = "Buscar entidades de Bairro",
        description = "Busca entidades de Bairro com base em filtros opcionais, como código do bairro, código do município, nome e status.",
        parameters = {
            @Parameter(name = "codigoBairro", description = "O identificador único do bairro.",example = "4"),
            @Parameter(name = "codigoMunicipio", description = "O identificador único do município associado.",example = "3"),
            @Parameter(name = "nome", description = "O nome do bairro.", example = "Pajuçara"),
            @Parameter(name = "status", description = "O status do bairro. 1 para ativo, 2 para inativo.", example = "1")
        },
        responses = {
            @ApiResponse(responseCode = "200",
                description = "Lista de bairros ou uma única entidade que corresponde aos filtros.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BairroResponse.class))),
            @ApiResponse(responseCode = "400",
                description = "Filtros inválidos ou mal formatados.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                description = "Erro interno no servidor.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<?> findBairro(
            @RequestParam(required = false) Long codigoBairro,
            @RequestParam(required = false) Long codigoMunicipio,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Map<String, String> allFilters
    ) {
        ControllerUtil.validateFilters(EXPECTED_FILTERS, allFilters, messageSource);

        if (ControllerUtil.isFiltersApplied(codigoBairro)) {
            Optional<Bairro> elementByFilters = bairroService.findElementByFilters(
                    codigoBairro, codigoMunicipio, nome, status);
            if (elementByFilters.isPresent()) {
                return ResponseEntity.ok(BairroMapper.toResponse(elementByFilters.get()));
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        }

        if (ControllerUtil.isFiltersApplied(codigoMunicipio, nome, status)) {
            var elementsByAppliedFilters = bairroService.getElementsByAppliedFilters(codigoMunicipio, nome, status);
            return ResponseEntity.ok(BairroMapper.toResponseList(elementsByAppliedFilters));
        }

        return ResponseEntity.ok(BairroMapper.toResponseList(bairroService.findAll()));
    }

    @PostMapping
    @Transactional
    @Operation(
        summary = "Salvar uma nova entidade de Bairro",
        description = "Cria uma nova entidade de Bairro com os dados fornecidos.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Objeto BairroDto contendo os detalhes do bairro.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BairroDto.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200",
                description = "Entidade de Bairro salva com sucesso.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = BairroResponse.class))),
            @ApiResponse(responseCode = "400",
                description = "Corpo da requisição inválido.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                description = "Código de Município não encontrado.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409",
                description = "Dados conflitantes ou recurso já existente.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                description = "Erro interno no servidor.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<List<BairroResponse>> save(@Valid @RequestBody BairroDto bairroDto) {
        var municipio = municipioService.getByCodigoMunicipio(bairroDto.codigoMunicipio());
        var bairro = new Bairro(bairroDto, municipio);
        bairroService.assertUniqueness(bairro.getMunicipio(), bairro.getNome());

        try {
            bairroService.save(bairro);
            return ResponseEntity.ok(BairroMapper.toResponseList(bairroService.findAll()));
        } catch (Exception e) {
            throw new EntityNotSavedException(
                    messageSource.getMessage("error.entity.not.saved", new Object[]{"bairro"}, LOCALE_PT_BR)
            );
        }
    }

    @PutMapping
    @Transactional
    @Operation(
        summary = "Atualizar uma entidade de Bairro existente",
        description = "Atualiza os dados de uma entidade de Bairro existente com base no BairroUpdateDto fornecido.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Objeto BairroUpdateDto contendo os detalhes atualizados.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BairroUpdateDto.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200",
                description = "Entidade de Bairro atualizada com sucesso.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = BairroResponse.class))),
            @ApiResponse(responseCode = "400",
                description = "Corpo da requisição inválido.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                description = "Entidade de Bairro ou Município não encontrada.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409",
                description = "Dados conflitantes ou recurso já existente.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                description = "Erro interno no servidor.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<List<BairroResponse>> update(@Valid @RequestBody BairroUpdateDto bairroUpdateDto) {
        var bairro = bairroService.getByCodigoBairro(bairroUpdateDto.codigoBairro());
        var municipio = municipioService.getByCodigoMunicipio(bairroUpdateDto.codigoMunicipio());
        bairroService.assertUpdatable(bairro, bairroUpdateDto);

        try {
            BeanUtils.copyProperties(bairroUpdateDto, bairro);
            bairro.setMunicipio(municipio);
            bairroService.save(bairro);
            return ResponseEntity.ok(BairroMapper.toResponseList(bairroService.findAll()));
        } catch (Exception e) {
            throw new EntityNotSavedException(
                    messageSource.getMessage("error.entity.not.saved", new Object[]{"bairro"}, LOCALE_PT_BR)
            );
        }
    }
 }
