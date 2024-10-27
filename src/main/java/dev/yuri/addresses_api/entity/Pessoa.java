package dev.yuri.addresses_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "TB_PESSOA")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_pessoa")
    @SequenceGenerator(name = "sequence_pessoa", sequenceName = "SEQUENCE_PESSOA", allocationSize = 1)
    @Column(name = "CODIGO_PESSOA", nullable = false)
    @EqualsAndHashCode.Include
    private Long codigoPessoa;

    @NotNull
    @Size(max = 256)
    @Column(name = "NOME", nullable = false)
    private String nome;

    @NotNull
    @Size(max = 256)
    @Column(name = "SOBRENOME", nullable = false)
    private String sobrenome;

    @NotNull
    @Min(0)
    @Max(150)
    @Column(name = "IDADE", nullable = false)
    private Integer idade;

    @NotNull
    @Size(max = 50)
    @Column(name = "LOGIN", nullable = false, unique = true)
    private String login;

    @NotNull
    @Size(max = 50)
    @Column(name = "SENHA", nullable = false)
    private String senha;

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private Integer status;
}


