package dev.yuri.addresses_api.entity;

import dev.yuri.addresses_api.dto.request.UFDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "TB_UF")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class UF {

    public UF (UFDto ufDto) {
        this.sigla = ufDto.sigla();
        this.nome = ufDto.nome();
        this.status = ufDto.status();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_uf")
    @SequenceGenerator(name = "sequence_uf", sequenceName = "SEQUENCE_UF", allocationSize = 1)
    @Column(name = "CODIGO_UF", nullable = false)
    @EqualsAndHashCode.Include
    private Long codigoUF;

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

