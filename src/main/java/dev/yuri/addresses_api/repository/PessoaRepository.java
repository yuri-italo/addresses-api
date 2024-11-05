package dev.yuri.addresses_api.repository;

import dev.yuri.addresses_api.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    @Query("SELECT p FROM Pessoa p " +
            "WHERE (:codigoPessoa IS NULL OR p.codigoPessoa = :codigoPessoa) " +
            "AND (:login IS NULL OR LOWER(p.login) LIKE LOWER(CONCAT('%', :login, '%'))) " +
            "AND (:status IS NULL OR p.status = :status)")
    Optional<Pessoa> getElementByFilters(@Param("codigoPessoa") Long codigoPessoa,
                                         @Param("login") String login,
                                         @Param("status") Integer status);

    @Query("SELECT p FROM Pessoa p " +
            "WHERE (:login IS NULL OR LOWER(p.login) LIKE LOWER(CONCAT('%', :login, '%'))) " +
            "AND (:status IS NULL OR p.status = :status)")
    List<Pessoa> getElementsByAppliedFields(@Param("login") String login,
                                            @Param("status") Integer status);
}
