package dev.yuri.addresses_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "TB_BAIRRO")
public class Bairro {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_bairro")
    @SequenceGenerator(name = "sequence_bairro", sequenceName = "SEQUENCE_BAIRRO", allocationSize = 1)
    @Column(name = "CODIGO_BAIRRO", nullable = false)
    @EqualsAndHashCode.Include
    private Long codigoBairro;

    @NotNull
    @Column(name = "CODIGO_MUNICIPIO", nullable = false)
    private Long codigoMunicipio;

    @NotNull
    @Size(max = 256)
    @Column(name = "NOME", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private Integer status;
}

