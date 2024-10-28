package dev.yuri.addresses_api.service;

import dev.yuri.addresses_api.entity.UF;
import dev.yuri.addresses_api.repository.UFRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UFService {

    private final UFRepository uFRepository;

    public UFService(UFRepository uFRepository) {
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
}
