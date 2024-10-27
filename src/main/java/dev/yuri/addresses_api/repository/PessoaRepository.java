package dev.yuri.addresses_api.repository;

import dev.yuri.addresses_api.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}
