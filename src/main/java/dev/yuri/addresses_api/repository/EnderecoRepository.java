package dev.yuri.addresses_api.repository;

import dev.yuri.addresses_api.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
