# MagicBox - Back-End

Este repositório contém o código-fonte do back-end do projeto **MagicBox**, uma aplicação desenvolvida em Java com Spring Boot, responsável por fornecer APIs RESTful, gerenciar a lógica de negócio e realizar a persistência de dados.

## Índice

- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Como Executar](#como-executar)
- [Testes](#testes)
- [APIs Disponíveis](#apis-disponíveis)
- [Contribuição](#contribuição)
- [Licença](#licença)

---

## Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot**
- **Spring Data JPA**
- **Maven**
- **Banco de Dados Relacional** (ex: PostgreSQL, MySQL)
- **Jackson** (serialização JSON)
- **JUnit** (testes automatizados)

---

## Pré-requisitos

Antes de começar, você precisará ter instalado em sua máquina:

- [Java 17 ou superior](https://adoptium.net/)
- [Maven 3.8+](https://maven.apache.org/)
- [Git](https://git-scm.com/)
- Um banco de dados relacional (PostgreSQL, MySQL, etc.)

---

## Instalação

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/magicbox.git
   cd magicbox/MagicBox/Back-End
   ```

2. **Configure o banco de dados:**
   - Crie um banco de dados no seu SGBD favorito (ex: PostgreSQL, MySQL).
   - Anote o nome, usuário e senha para configurar no próximo passo.

---

## Configuração

Edite o arquivo `src/main/resources/application.properties` com as informações do seu banco de dados. Exemplo para PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/magicbox
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Outras configurações podem ser ajustadas conforme necessário.

---

## Estrutura do Projeto

```
Back-End/
├── mvnw, mvnw.cmd, pom.xml      # Scripts e configuração Maven
├── src/
│   ├── main/
│   │   ├── java/com/magicbox/backend/
│   │   │   ├── config/           # Configurações globais (ex: CORS)
│   │   │   ├── controller/       # Controladores REST
│   │   │   ├── mapper/           # Utilitários de deserialização
│   │   │   ├── model/
│   │   │   │   ├── dto/          # Data Transfer Objects (DTOs)
│   │   │   │   └── entity/       # Entidades JPA (tabelas do banco)
│   │   │   ├── repository/       # Interfaces de acesso ao banco
│   │   │   └── service/          # Lógica de negócio
│   │   └── resources/
│   │       ├── application.properties # Configurações da aplicação
│   │       ├── static/           # Arquivos estáticos (opcional)
│   │       └── templates/        # Templates (opcional)
│   └── test/
│       └── java/com/magicbox/backend/ # Testes automatizados
└── target/                       # Artefatos gerados (não versionar)
```

---

## Como Executar

1. **Build do projeto:**
   ```bash
   ./mvnw clean install
   ```

2. **Executar a aplicação:**
   ```bash
   ./mvnw spring-boot:run
   ```
   A aplicação estará disponível em: [http://localhost:8080](http://localhost:8080)

---

## Testes

Para rodar os testes automatizados:

```bash
./mvnw test
```

---

## APIs Disponíveis

A aplicação expõe diversas APIs RESTful para gerenciamento de entidades como Usuário, Música, Artista, Álbum, País, Tag, Rankings, entre outros.

- Os endpoints seguem o padrão REST e podem ser acessados via `/api/...`
- Para detalhes de cada endpoint, recomenda-se utilizar ferramentas como [Swagger](https://swagger.io/) ou [Postman](https://www.postman.com/).

---

## Contribuição

Contribuições são bem-vindas! Para contribuir:

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b minha-feature`)
3. Commit suas alterações (`git commit -m 'Minha nova feature'`)
4. Push para a branch (`git push origin minha-feature`)
5. Abra um Pull Request

---

## Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](../LICENSE) para mais detalhes.

---

Se precisar de um exemplo de requisição, documentação de endpoints ou integração com o front-end, é só pedir! 