package dev.yuri.addresses_api.service;

import dev.yuri.addresses_api.entity.UF;
import dev.yuri.addresses_api.exception.ResourceNotSavedException;
import dev.yuri.addresses_api.repository.UFRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UFService {

    private final MessageSource messageSource;
    private final UFRepository uFRepository;

    public UFService(MessageSource messageSource, UFRepository uFRepository) {
        this.messageSource = messageSource;
        this.uFRepository = uFRepository;
    }

    public Optional<List<UF>> findAll() {
        return Optional.of(uFRepository.findAll());
    }

    public Optional<UF> findElementByFilters(Long codigoUF, String sigla, String nome, Integer status) {
        return uFRepository.getElementByFilters(codigoUF, sigla, nome, status);
    }

    public Optional<List<UF>> findElementsByStatus(Integer status) {
        return uFRepository.findByStatus(status);
    }

    public UF save(UF uf) {
        try {
            return uFRepository.save(uf);
        } catch (Exception e) {
            throw new ResourceNotSavedException(messageSource.getMessage("error.post", new Object[]{"UF"},
                new Locale("pt", "BR")
            ));
        }
    }
}