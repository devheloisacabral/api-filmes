# Cadastro de Filmes API

Projeto Java com Spring Boot para cadastro de filmes.

## Como executar no IntelliJ IDEA

1. Abra a pasta do projeto no IntelliJ IDEA.
2. Aguarde o IntelliJ importar o `pom.xml`.
3. Execute a classe `CadastroDeFilmesApplication`.
4. A API ficará disponível em `http://localhost:8080`.

Também é possível executar pelo terminal:

```bash
mvn spring-boot:run
```

## Endpoints

- `GET /filmes` — lista os filmes cadastrados.
- `GET /filmes/{id}` — busca um filme pelo ID.
- `POST /filmes` — cadastra um filme.
- `PUT /filmes/{id}` — atualiza um filme.
- `DELETE /filmes/{id}` — remove um filme.

Exemplo de cadastro:

```json
{
  "titulo": "A Origem",
  "genero": "Ficção científica",
  "anoLancamento": 2010
}
```

O sistema começa com três filmes cadastrados: `O Senhor dos Anéis: A Sociedade do Anel`, `Interestelar` e `Parasita`.

Os dados são mantidos em memória e são reiniciados quando a aplicação é encerrada.
