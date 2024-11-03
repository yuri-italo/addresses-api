package dev.yuri.addresses_api.entity;

import dev.yuri.addresses_api.dto.request.BairroDto;
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
@Table(name = "TB_BAIRRO")
public class Bairro {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_bairro")
    @SequenceGenerator(name = "sequence_bairro", sequenceName = "SEQUENCE_BAIRRO", allocationSize = 1)
    @Column(name = "CODIGO_BAIRRO", nullable = false)
    @EqualsAndHashCode.Include
    private Long codigoBairro;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "CODIGO_MUNICIPIO", nullable = false)
    private Municipio municipio;

    @NotNull
    @Size(max = 256)
    @Column(name = "NOME", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private Integer status;

    public Bairro(@Valid BairroDto bairroDto, Municipio municipio) {
        this.municipio = Objects.requireNonNull(municipio);
        this.nome = bairroDto.nome();
        this.status = bairroDto.status();
    }
}

