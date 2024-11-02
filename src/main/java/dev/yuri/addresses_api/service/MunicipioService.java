package dev.yuri.addresses_api.service;

import dev.yuri.addresses_api.entity.Municipio;
import dev.yuri.addresses_api.entity.UF;
import dev.yuri.addresses_api.exception.EntityNotFoundException;
import dev.yuri.addresses_api.repository.MunicipioRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static dev.yuri.addresses_api.controller.MunicipioController.LOCALE_PT_BR;

@Service
public class MunicipioService {
    private final MunicipioRepository municipioRepository;
    private final UFService uFService;
    private final MessageSource messageSource;

    public MunicipioService(MunicipioRepository municipioRepository, UFService uFService, MessageSource messageSource) {
        this.municipioRepository = municipioRepository;
        this.uFService = uFService;
        this.messageSource = messageSource;
    }

    public Optional<Municipio> findElementByFilters(Long codigoMunicipio, Long codigoUF, String nome, Integer status) {
        return municipioRepository.getElementByFilters(codigoMunicipio,codigoUF, nome, status);
    }

    public List<Municipio> findElementsByAppliedFilters(Long codigoUF, String nome, Integer status) {
        UF uF = null;
        if (codigoUF != null) {
            uF = uFService.getByCodigoUF(codigoUF).orElseThrow(() -> new EntityNotFoundException(
                    messageSource.getMessage("error.entity.not.exists", new Object[]{"UF", codigoUF}, LOCALE_PT_BR))
            );
        }

        return municipioRepository.getElementsByAppliedFields(uF, nome, status);
    }

    public List<Municipio> findAll() {
        return municipioRepository.findAll();
    }
}
