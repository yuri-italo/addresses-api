package dev.yuri.addresses_api.entity;

import dev.yuri.addresses_api.dto.request.PessoaDto;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
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

    public Pessoa(@Valid PessoaDto pessoaDto) {
        this.nome = pessoaDto.nome();
        this.sobrenome = pessoaDto.sobrenome();
        this.idade = pessoaDto.idade();
        this.login = pessoaDto.login();
        this.senha = pessoaDto.senha();
        this.status = pessoaDto.status();
    }
}


