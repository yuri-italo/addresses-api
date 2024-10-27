package dev.yuri.addresses_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "TB_ENDERECO")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_endereco")
    @SequenceGenerator(name = "sequence_endereco", sequenceName = "SEQUENCE_ENDERECO", allocationSize = 1)
    @Column(name = "CODIGO_ENDERECO", nullable = false)
    @EqualsAndHashCode.Include
    private Long codigoEndereco;

    @NotNull
    @Column(name = "CODIGO_PESSOA", nullable = false)
    private Long codigoPessoa;

    @NotNull
    @Column(name = "CODIGO_BAIRRO", nullable = false)
    private Long codigoBairro;

    @NotNull
    @Size(max = 256)
    @Column(name = "NOME_RUA", nullable = false)
    private String nomeRua;

    @NotNull
    @Size(max = 10)
    @Column(name = "NUMERO", nullable = false)
    private String numero;

    @Size(max = 20)
    @Column(name = "COMPLEMENTO")
    private String complemento;

    @NotNull
    @Size(max = 10)
    @Column(name = "CEP", nullable = false, unique = true)
    private String cep;
}
