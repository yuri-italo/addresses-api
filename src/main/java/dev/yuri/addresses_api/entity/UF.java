package dev.yuri.addresses_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "TB_UF")
public class UF {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_uf")
    @SequenceGenerator(name = "sequence_uf", sequenceName = "SEQUENCE_UF", allocationSize = 1)
    @Column(name = "CODIGO_UF", nullable = false)
    @EqualsAndHashCode.Include
    private Long codigoUf;

    @NotNull
    @Size(max = 3)
    @Column(name = "SIGLA", nullable = false, unique = true)
    private String sigla;

    @NotNull
    @Size(max = 60)
    @Column(name = "NOME", nullable = false, unique = true)
    private String nome;

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private Integer status;
}

