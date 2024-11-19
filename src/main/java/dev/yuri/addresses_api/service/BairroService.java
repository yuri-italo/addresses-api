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

    public Optional<Bairro> findByMunicipioAndNome(Municipio municipio, String nome) {
        return bairroRepository.findByMunicipioAndNome(municipio, nome);
    }

    public Optional<Bairro> findElementByFilters(Long codigoBairro, Long codigoMunicipio, String nome, Integer status) {
        return bairroRepository.findElementByCodigoBairroOrCodigoMunicipioOrNomeOrStatus(
                codigoBairro, codigoMunicipio, nome, status);
    }

    public List<Bairro> getElementsByAppliedFilters(Long codigoMunicipio, String nome, Integer status) {
        Municipio municipio = null;
        if (codigoMunicipio != null) {
            municipio = municipioService.getByCodigoMunicipio(codigoMunicipio);
        }

        return this.getElementsByMunicipioOrNomeOrStatus(municipio, nome, status);
    }

    public List<Bairro> getElementsByMunicipioOrNomeOrStatus(Municipio municipio, String nome, Integer status) {
        return bairroRepository.getElementsByMunicipioOrNomeOrStatus(municipio, nome, status);
    }

    public List<Bairro> findAll() {
        return bairroRepository.findAll();
    }

    public void assertUniqueness(Bairro bairro) {
        var municipio = bairro.getMunicipio();
        var nome = bairro.getNome();

        this.findByMunicipioAndNome(municipio, nome)
            .ifPresent(b -> {
                throw new EntityAlreadyExistsException(messageSource.getMessage("error.bairro.already.exists",
                  new Object[]{nome, municipio.getCodigoMunicipio()}, BairroController.LOCALE_PT_BR));
            });
    }

    public void save(Bairro bairro) {
       bairroRepository.save(bairro);
    }

    public Bairro getByCodigoBairro(Long codigoBairro) {
        return bairroRepository.findById(codigoBairro)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.entity.not.exists",
                        new Object[]{"bairro", codigoBairro}, BairroController.LOCALE_PT_BR)));
    }

    public void assertUpdatable(Bairro bairro, BairroUpdateDto bairroUpdateDto) {
        if (hasSameAttributes(bairro, bairroUpdateDto)) {
            return;
        }

        var municipio = municipioService.getByCodigoMunicipio(bairroUpdateDto.codigoMunicipio());
        var bairroUpdate = new Bairro(bairroUpdateDto, municipio);

        this.assertUniqueness(bairroUpdate);
    }

    private boolean hasSameAttributes(Bairro bairro, BairroUpdateDto bairroUpdateDto) {
        return bairro.getMunicipio().getCodigoMunicipio().equals(bairroUpdateDto.codigoMunicipio()) &&
                bairro.getNome().equalsIgnoreCase(bairroUpdateDto.nome());
    }
}
