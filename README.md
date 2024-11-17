# ENDEREÇOS API

API REST para o gerenciamento de endereços. Ela permite a criação, leitura, atualização e exclusão de dados de endereços, utilizando Spring Boot, JPA, Flyway para migração de banco de dados e OpenAPI para documentação da API.

## Descrição

Este projeto foi desenvolvido para facilitar o gerenciamento de endereços de forma simples e eficiente, com integração ao banco de dados Oracle utilizando Flyway para gerenciamento de migrações.

## Tecnologias Utilizadas

- **Spring Boot**: Framework principal para desenvolvimento da aplicação.
- **Spring Data JPA**: Para integração com o banco de dados.
- **Spring Validation**: Para validações dos dados recebidos via API.
- **Springdoc OpenAPI**: Para gerar e disponibilizar a documentação da API.
- **Flyway**: Para migrações de banco de dados.
- **Oracle Database**: Banco de dados utilizado.
- **Lombok**: Para redução de boilerplate code (getter, setter, construtores, etc).

## Requisitos

- **Java 17**: A aplicação foi construída utilizando o Java 17.
- **Oracle Database**: Banco de dados utilizado (requer o driver `ojdbc11`).
- **Maven**: Para gerenciamento de dependências e build.

## Instalação

### 1. Clone o repositório:

```bash
git clone https://github.com/yuri-italo/addresses-api.git
cd addresses-api
```

### 2. Compile o projeto:

```bash
mvn clean install
```

### 3. Execute a aplicação:

```bash
mvn spring-boot:run
```

Ou, se preferir, pode executar o JAR gerado:

```bash
java -jar target/addresses-api-0.0.1-SNAPSHOT.jar
```

## Endpoints

A API pode ser acessada pelos seguintes endpoints:

### **UF**

- `POST /uf`: Criação de uma nova UF.
- `GET /uf`: Busca uma UF ou uma lista.
- `PUT /uf`: Atualiza os dados de uma UF existente.

### **Município**

- `POST /municipio`: Criação de um novo município.
- `GET /municipio`: Busca um município ou uma lista.
- `PUT /municipio`: Atualiza os dados de um município existente.

### **Bairro**

- `POST /bairro`: Criação de um novo bairro.
- `GET /bairro`: Busca um bairro ou uma lista.
- `PUT /bairro`: Atualiza os dados de um bairro existente.


### **Pessoa**

- `POST /pessoa`: Criação de uma nova pessoa.
- `GET /pessoa/`: Busca uma pessoa ou uma lista.
- `PUT /pessoa/`: Atualiza os dados de uma pessoa existente.

### **Documentação OpenAPI**

A documentação da API pode ser acessada em:

```
http://localhost:8080/swagger-ui/index.html
```