package dev.yuri.addresses_api.controller;

import dev.yuri.addresses_api.dto.request.PessoaDto;
import dev.yuri.addresses_api.dto.request.PessoaUpdateDto;
import dev.yuri.addresses_api.dto.response.PessoaResponse;
import dev.yuri.addresses_api.entity.Endereco;
import dev.yuri.addresses_api.entity.Pessoa;
import dev.yuri.addresses_api.exception.EntityNotSavedException;
import dev.yuri.addresses_api.mapper.PessoaMapper;
import dev.yuri.addresses_api.service.EnderecoService;
import dev.yuri.addresses_api.service.PessoaService;
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
@RequestMapping("/pessoa")
public class PessoaController {
    private final PessoaService pessoaService;
    private final EnderecoService enderecoService;
    private final MessageSource messageSource;
    private static final List<String> EXPECTED_FILTERS = Arrays.asList("codigoPessoa", "login", "status");
    public static final Locale LOCALE_PT_BR = new Locale("pt", "BR");

    public PessoaController(PessoaService pessoaService, EnderecoService enderecoService, MessageSource messageSource) {
        this.pessoaService = pessoaService;
        this.enderecoService = enderecoService;
        this.messageSource = messageSource;
    }

    @GetMapping
    @Operation(
        summary = "Buscar entidades de Pessoa",
        description = "Busca entidades de Pessoa com base em filtros opcionais, como código da pessoa, login e status.",
        parameters = {
            @Parameter(name = "codigoPessoa",
                description = "O identificador único da pessoa. Exemplo: 123",
                schema = @Schema(type = "long", example = "123",  description = "Exemplo de código Pessoa")),
            @Parameter(name = "login",
                description = "O login único da pessoa. Exemplo: usuario123",
                schema = @Schema(type = "string", example = "usuario123", description = "Exemplo de login")),
            @Parameter(name = "status",
                description = "O status da pessoa. Exemplo: 1 para ativo, 0 para inativo.",
                schema = @Schema(type = "integer", example = "1", description = "Exemplo de status"))
        },
        responses = {
            @ApiResponse(responseCode = "200",
                description = "Lista de pessoas ou uma única entidade que corresponde aos filtros.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400",
                description = "Filtros inválidos ou mal formatados.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500",
                description = "Erro interno no servidor.",
                content = @Content(mediaType = "application/json"))
        }
    )
    public ResponseEntity<?> findPessoa(
            @RequestParam(required = false) Long codigoPessoa,
            @RequestParam(required = false) String login,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Map<String, String> allFilters
    ) {
        ControllerUtil.validateFilters(EXPECTED_FILTERS, allFilters, messageSource);

        if (ControllerUtil.isFiltersApplied(codigoPessoa)) {
            Optional<Pessoa> elementByFilters = pessoaService.findElementByFilters(codigoPessoa, login, status);
            if (elementByFilters.isPresent()) {
                var pessoa = elementByFilters.get();
                var enderecos = pessoaService.getEnderecosByPessoa(pessoa);
                return ResponseEntity.ok(PessoaMapper.toResponse(pessoa, enderecos));
            } else {
                return ResponseEntity.ok(Collections.emptyList());
            }
        }

        if (ControllerUtil.isFiltersApplied(login, status)) {
            var elementsByAppliedFilters = pessoaService.getElementsByAppliedFilters(login, status);
            return ResponseEntity.ok(PessoaMapper.toResponseList(elementsByAppliedFilters));
        }

        return ResponseEntity.ok(PessoaMapper.toResponseList(pessoaService.findAll()));
    }

    @PostMapping
    @Transactional
    @Operation(
        summary = "Salvar uma nova entidade de Pessoa",
        description = "Cria uma nova entidade de Pessoa com os dados fornecidos no corpo da requisição.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Objeto PessoaDto contendo os detalhes da pessoa.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PessoaDto.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200",
                description = "Entidade de Pessoa salva com sucesso.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400",
                description = "Corpo da requisição inválido.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404",
                description = "Código de Pessoa ou Bairro não encontrado.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409",
                description = "Dados conflitantes ou recurso já existente.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500",
                description = "Erro interno no servidor.",
                content = @Content(mediaType = "application/json"))
        }
    )
    public ResponseEntity<List<PessoaResponse>> save(@Valid @RequestBody PessoaDto pessoaDto) {
        var pessoa = pessoaService.save(pessoaDto);
        var enderecos = enderecoService.toEntityList(pessoaDto, pessoa);

        try {
            enderecoService.saveAll(enderecos);
            return ResponseEntity.ok(PessoaMapper.toResponseList(pessoaService.findAll()));
        } catch (Exception e) {
            throw new EntityNotSavedException(
                    messageSource.getMessage("error.entity.not.saved", new Object[]{"pessoa"}, LOCALE_PT_BR)
            );
        }
    }

    @PutMapping
    @Transactional
    @Operation(
        summary = "Atualizar uma entidade de Pessoa existente",
        description = "Atualiza os dados de uma entidade de Pessoa existente com base no PessoaUpdateDto fornecido.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Objeto PessoaUpdateDto contendo os detalhes atualizados.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PessoaUpdateDto.class))
        ),
        responses = {
            @ApiResponse(responseCode = "200",
                description = "Entidade de Pessoa atualizada com sucesso.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400",
                description = "Corpo da requisição inválido.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404",
                description = "Entidade de Pessoa, Endereço ou Bairro não encontrada.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409",
                description = "Dados conflitantes ou recurso já existente.",
                content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500",
                description = "Erro interno no servidor.",
                content = @Content(mediaType = "application/json"))
        }
    )
    public ResponseEntity<List<PessoaResponse>> update(@Valid @RequestBody PessoaUpdateDto pessoaUpdateDto) {
        var pessoa = pessoaService.getByCodigoPessoa(pessoaUpdateDto.codigoPessoa());
        pessoaService.assertUpdatable(pessoa, pessoaUpdateDto);

        try {
            BeanUtils.copyProperties(pessoaUpdateDto, pessoa);
            pessoa = pessoaService.save(pessoa);
        } catch (Exception e) {
            throw new EntityNotSavedException(
                    messageSource.getMessage("error.entity.not.saved", new Object[]{"pessoa"}, LOCALE_PT_BR)
            );
        }

        List<Endereco> enderecoList = enderecoService.toEntityList(pessoaUpdateDto.enderecos());
        enderecoService.assertUpdatable(enderecoList, pessoa);

        try {
            enderecoService.saveAll(enderecoList, pessoa);
            return ResponseEntity.ok(PessoaMapper.toResponseList(pessoaService.findAll()));
        } catch (Exception e) {
            throw new EntityNotSavedException(
                    messageSource.getMessage("error.entity.not.saved", new Object[]{"endereco"}, LOCALE_PT_BR)
            );
        }
    }
}
