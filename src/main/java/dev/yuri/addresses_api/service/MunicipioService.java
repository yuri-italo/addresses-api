package dev.yuri.addresses_api.service;

import dev.yuri.addresses_api.controller.MunicipioController;
import dev.yuri.addresses_api.dto.request.MunicipioUpdateDto;
import dev.yuri.addresses_api.entity.Municipio;
import dev.yuri.addresses_api.entity.UF;
import dev.yuri.addresses_api.exception.EntityAlreadyExistsException;
import dev.yuri.addresses_api.repository.MunicipioRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
            uF = uFService.getByCodigoUF(codigoUF);
        }

        return municipioRepository.getElementsByAppliedFields(uF, nome, status);
    }

    public List<Municipio> findAll() {
        return municipioRepository.findAll();
    }

    public void save(Municipio municipio) {
        municipioRepository.save(municipio);
    }

    public void assertUniqueness(UF uf, String nome) {
        var optionalMunicipio = municipioRepository.findByUfAndNome(uf, nome);

        if (optionalMunicipio.isPresent()) {
            throw new EntityAlreadyExistsException(
                    messageSource.getMessage("error.entity.already.exists",
                            new Object[]{"município", "nome", nome}, MunicipioController.LOCALE_PT_BR)
            );
        }
    }

    public Optional<Municipio> getByCodigoMunicipio(Long codigoMunicipio) {
      return   municipioRepository.findById(codigoMunicipio);
    }

    public void assertUpdatable(Municipio municipio, MunicipioUpdateDto municipioUpdateDto) {
        if (hasSameAttributes(municipio, municipioUpdateDto)) {
            return;
        }

        var codigoUF = municipioUpdateDto.codigoUF();
        var uF = uFService.getByCodigoUF(codigoUF);

        this.assertUniqueness(uF, municipioUpdateDto.nome());
    }

    private boolean hasSameAttributes(Municipio municipio, MunicipioUpdateDto municipioUpdateDto) {
        return municipio.getUf().getCodigoUF().equals(municipioUpdateDto.codigoUF()) &&
                municipio.getNome().equals(municipioUpdateDto.nome());
    }
}
