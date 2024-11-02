package dev.yuri.addresses_api.entity;

import dev.yuri.addresses_api.dto.request.MunicipioDto;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "TB_MUNICIPIO")
public class Municipio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_municipio")
    @SequenceGenerator(name = "sequence_municipio", sequenceName = "SEQUENCE_MUNICIPIO", allocationSize = 1)
    @Column(name = "CODIGO_MUNICIPIO", nullable = false)
    @EqualsAndHashCode.Include
    private Long codigoMunicipio;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "CODIGO_UF", nullable = false)
    private UF uf;

    @Size(max = 256)
    @Column(name = "NOME")
    private String nome;

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private Integer status;

    public Municipio(@Valid MunicipioDto municipioDto, UF uF) {
        this.uf = Objects.requireNonNull(uF);
        this.nome = municipioDto.nome();
        this.status = municipioDto.status();
    }
}

