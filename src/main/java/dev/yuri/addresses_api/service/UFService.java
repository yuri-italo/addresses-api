package dev.yuri.addresses_api.service;

import dev.yuri.addresses_api.dto.request.UFUpdateDto;
import dev.yuri.addresses_api.entity.UF;
import dev.yuri.addresses_api.exception.EntityAlreadyExistsException;
import dev.yuri.addresses_api.exception.EntityNotFoundException;
import dev.yuri.addresses_api.repository.UFRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static dev.yuri.addresses_api.controller.UFController.LOCALE_PT_BR;

@Service
public class UFService {

    private final MessageSource messageSource;
    private final UFRepository uFRepository;

    public UFService(MessageSource messageSource, UFRepository uFRepository) {
        this.messageSource = messageSource;
        this.uFRepository = uFRepository;
    }

    public List<UF> findAll() {
        return uFRepository.findAll();
    }

    Optional<UF> findByNome(String nome) {
        return uFRepository.findByNome(nome);
    }

    Optional<UF> findBySigla(String sigla) {
        return uFRepository.findBySigla(sigla);
    }

    public Optional<UF> findElementByFilters(Long codigoUF, String sigla, String nome, Integer status) {
        return uFRepository.findElementByCodigoUFOrSiglaOrNomeOrStatus(codigoUF, sigla, nome, status);
    }

    public List<UF> getElementsByStatus(Integer status) {
        return uFRepository.getByStatus(status);
    }

    public void save(UF uf) {
        uFRepository.save(uf);
    }

    public UF getByCodigoUF(Long codigoUF) {
        return  uFRepository.findById(codigoUF)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("error.entity.not.exists",
                        new Object[]{"UF", codigoUF}, LOCALE_PT_BR))
        );
    }

    public void assertUpdatable(UF uf, UFUpdateDto ufUpdateDto) {
        if (hasSameAttributes(uf, ufUpdateDto)) {
            return;
        }

        assertUniquenessByNome(ufUpdateDto.nome());
        assertUniquenessBySigla(ufUpdateDto.sigla());
    }

    private boolean hasSameAttributes(UF uf, UFUpdateDto ufUpdateDto) {
        return uf.getNome().equals(ufUpdateDto.nome()) &&
                uf.getSigla().equals(ufUpdateDto.sigla());
    }

    public void assertUniqueness(UF uf) {
        assertUniquenessByNome(uf.getNome());
        assertUniquenessBySigla(uf.getSigla());
    }

    private void assertUniquenessBySigla(String sigla) {
        this.findBySigla(sigla)
                .ifPresent(uf -> {
                    throw new EntityAlreadyExistsException(messageSource.getMessage("error.entity.already.exists",
                            new Object[]{"UF", "sigla", sigla}, LOCALE_PT_BR));
                });
    }

    private void assertUniquenessByNome(String nome) {
        this.findByNome(nome)
                .ifPresent(uf -> {
                    throw new EntityAlreadyExistsException(messageSource.getMessage("error.entity.already.exists",
                            new Object[]{"UF", "nome", nome}, LOCALE_PT_BR));
                });
    }
}