ALTER TABLE TB_PESSOA
ADD CONSTRAINT UQ_LOGIN UNIQUE (LOGIN);

ALTER TABLE TB_PESSOA
ADD CONSTRAINT CHK_STATUS_PESSOA CHECK (STATUS IN (0, 1));

ALTER TABLE TB_UF
ADD CONSTRAINT UQ_SIGLA UNIQUE (SIGLA);

ALTER TABLE TB_UF
ADD CONSTRAINT UQ_NOME_UF UNIQUE (NOME);

ALTER TABLE TB_UF
ADD CONSTRAINT CHK_STATUS_UF CHECK (STATUS IN (0, 1));

ALTER TABLE TB_MUNICIPIO
ADD CONSTRAINT CHK_STATUS_MUNICIPIO CHECK (STATUS IN (0, 1));

ALTER TABLE TB_BAIRRO
ADD CONSTRAINT CHK_STATUS_BAIRRO CHECK (STATUS IN (0, 1));

ALTER TABLE TB_ENDERECO
ADD CONSTRAINT UQ_CEP UNIQUE (CEP);
