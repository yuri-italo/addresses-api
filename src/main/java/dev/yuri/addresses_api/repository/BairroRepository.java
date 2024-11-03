package dev.yuri.addresses_api.repository;

import dev.yuri.addresses_api.entity.Bairro;
import dev.yuri.addresses_api.entity.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BairroRepository extends JpaRepository<Bairro, Long> {
    @Query("SELECT b FROM Bairro b " +
            "WHERE (:codigoBairro IS NULL OR b.codigoBairro = :codigoBairro) " +
            "AND (:codigoMunicipio IS NULL OR b.municipio.codigoMunicipio = :codigoMunicipio) " +
            "AND (:nome IS NULL OR LOWER(b.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
            "AND (:status IS NULL OR b.status = :status)")
    Optional<Bairro> getElementByFilters(@Param("codigoBairro") Long codigoBairro,
                                         @Param("codigoMunicipio") Long codigoMunicipio,
                                         @Param("nome") String nome,
                                         @Param("status") Integer status);

    @Query("SELECT b FROM Bairro b " +
            "WHERE (:municipio IS NULL OR b.municipio = :municipio) " +
            "AND (:nome IS NULL OR LOWER(b.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
            "AND (:status IS NULL OR b.status = :status)")
    List<Bairro> getElementsByAppliedFields(@Param("municipio") Municipio municipio,
                                            @Param("nome") String nome,
                                            @Param("status") Integer status);

    Optional<Bairro> findByMunicipioAndNome(Municipio municipio, String nome);
}
