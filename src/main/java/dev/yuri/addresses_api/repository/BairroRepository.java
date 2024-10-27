package dev.yuri.addresses_api.repository;

import dev.yuri.addresses_api.entity.Bairro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BairroRepository extends JpaRepository<Bairro, Long> {
}
