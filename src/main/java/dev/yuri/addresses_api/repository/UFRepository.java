package dev.yuri.addresses_api.repository;

import dev.yuri.addresses_api.entity.UF;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UFRepository extends JpaRepository<UF, Long> {
}
