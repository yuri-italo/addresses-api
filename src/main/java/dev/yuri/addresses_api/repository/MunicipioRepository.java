package dev.yuri.addresses_api.repository;

import dev.yuri.addresses_api.entity.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MunicipioRepository extends JpaRepository<Municipio, Long> {
}
