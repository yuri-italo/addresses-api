package dev.yuri.addresses_api.service;

import dev.yuri.addresses_api.controller.BairroController;
import dev.yuri.addresses_api.dto.request.BairroUpdateDto;
import dev.yuri.addresses_api.entity.Bairro;
import dev.yuri.addresses_api.entity.Municipio;
import dev.yuri.addresses_api.exception.EntityAlreadyExistsException;
import dev.yuri.addresses_api.exception.EntityNotFoundException;
import dev.yuri.addresses_api.repository.BairroRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static dev.yuri.addresses_api.controller.MunicipioController.LOCALE_PT_BR;

@Service
public class BairroService {
    private final BairroRepository bairroRepository;
    private final MunicipioService municipioService;
    private final MessageSource messageSource;

    public BairroService(BairroRepository bairroRepository, MunicipioService municipioService, MessageSource messageSource) {
        this.bairroRepository = bairroRepository;
        this.municipioService = municipioService;
        this.messageSource = messageSource;
    }


    public Optional<Bairro> findElementByFilters(Long codigoBairro, Long codigoMunicipio, String nome, Integer status) {
        return bairroRepository.getElementByFilters(codigoBairro, codigoMunicipio, nome, status);
    }

    public List<Bairro> findElementsByAppliedFilters(Long codigoMunicipio, String nome, Integer status) {
        Municipio municipio = null;
        if (codigoMunicipio != null) {
            municipio = municipioService.getByCodigoMunicipio(codigoMunicipio)
                    .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage(
                            "error.entity.not.exists", new Object[]{"município", codigoMunicipio}, LOCALE_PT_BR))
            );
        }

        return bairroRepository.getElementsByAppliedFields(municipio, nome, status);
    }

    public List<Bairro> findAll() {
        return bairroRepository.findAll();
    }

    public void assertUniqueness(Municipio municipio, String nome) {
        var optionalBairro = bairroRepository.findByMunicipioAndNome(municipio, nome);

        if (optionalBairro.isPresent()) {
            throw new EntityAlreadyExistsException(
                    messageSource.getMessage("error.entity.already.exists",
                            new Object[]{"bairro", "nome", nome}, BairroController.LOCALE_PT_BR)
            );
        }
    }

    public void save(Bairro bairro) {
       bairroRepository.save(bairro);
    }

    public Optional<Bairro> getByCodigoBairro(Long codigoBairro) {
        return bairroRepository.findById(codigoBairro);
    }

    public void assertUpdatable(Bairro bairro, BairroUpdateDto bairroUpdateDto) {
        if (hasSameAttributes(bairro, bairroUpdateDto)) {
            return;
        }

        var codigoMunicipio = bairroUpdateDto.codigoMunicipio();
        var municipio = municipioService.getByCodigoMunicipio(codigoMunicipio)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.entity.not.exists",
                        new Object[]{"município", codigoMunicipio}, LOCALE_PT_BR)));

        this.assertUniqueness(municipio, bairroUpdateDto.nome());
    }

    private boolean hasSameAttributes(Bairro bairro, BairroUpdateDto bairroUpdateDto) {
        return bairro.getMunicipio().getCodigoMunicipio().equals(bairroUpdateDto.codigoMunicipio()) &&
                bairro.getNome().equals(bairroUpdateDto.nome());
    }
}
