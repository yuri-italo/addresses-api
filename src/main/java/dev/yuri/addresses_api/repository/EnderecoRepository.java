package dev.yuri.addresses_api.repository;

import dev.yuri.addresses_api.entity.Endereco;
import dev.yuri.addresses_api.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    List<Endereco> findByPessoa(Pessoa pessoa);
}
