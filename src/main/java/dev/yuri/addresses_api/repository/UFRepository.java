package dev.yuri.addresses_api.repository;

import dev.yuri.addresses_api.entity.UF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UFRepository extends JpaRepository<UF, Long> {
    @Query("SELECT u FROM UF u " +
            "WHERE (:codigoUF IS NULL OR u.codigoUF = :codigoUF) " +
            "AND (:sigla IS NULL OR LOWER(u.sigla) LIKE LOWER(CONCAT('%', :sigla, '%'))) " +
            "AND (:nome IS NULL OR LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
            "AND (:status IS NULL OR u.status = :status)")
    Optional<UF> findElementByCodigoUFAndSiglaAndNomeAndStatus(@Param("codigoUF") Long codigoUF,
                                                               @Param("sigla") String sigla,
                                                               @Param("nome") String nome,
                                                               @Param("status") Integer status);

    List<UF> getByStatus(Integer status);

    @Query("SELECT u FROM UF u WHERE LOWER(u.nome) = LOWER(:nome)")
    Optional<UF> findByNome(@Param("nome") String nome);

    @Query("SELECT u FROM UF u WHERE LOWER(u.sigla) = LOWER(:sigla)")
    Optional<UF> findBySigla(@Param("sigla") String sigla);
}
