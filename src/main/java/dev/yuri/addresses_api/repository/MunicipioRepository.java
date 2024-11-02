package dev.yuri.addresses_api.repository;

import dev.yuri.addresses_api.entity.Municipio;
import dev.yuri.addresses_api.entity.UF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MunicipioRepository extends JpaRepository<Municipio, Long> {
    @Query("SELECT m FROM Municipio m " +
            "WHERE (:codigoMunicipio IS NULL OR m.codigoMunicipio = :codigoMunicipio) " +
            "AND (:codigoUF IS NULL OR m.uf.codigoUF = :codigoUF) " +
            "AND (:nome IS NULL OR LOWER(m.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
            "AND (:status IS NULL OR m.status = :status)")
    Optional<Municipio> getElementByFilters(@Param("codigoMunicipio") Long codigoMunicipio,
                                            @Param("codigoUF") Long codigoUF,
                                            @Param("nome") String nome,
                                            @Param("status") Integer status);

    @Query("SELECT m FROM Municipio m " +
            "WHERE (:uF IS NULL OR m.uf = :uF) " +
            "AND (:nome IS NULL OR LOWER(m.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
            "AND (:status IS NULL OR m.status = :status)")
    List<Municipio> getElementsByAppliedFields(@Param("uF") UF uF,
                                               @Param("nome") String nome,
                                               @Param("status") Integer status);
}
