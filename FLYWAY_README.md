# Database Migrations com Flyway

Este projeto usa o Flyway para gerenciar migrations e versionamento do banco de dados.

## Estrutura dos Bancos de Dados

- **PostgreSQL Container**: Um único container PostgreSQL roda múltiplos bancos
- **trip_service_db**: Banco de dados do serviço de viagens
- **suggestion_service_db**: Banco de dados do serviço de sugestões

## Como funciona

### 1. Inicialização dos Bancos

O script `db-init/init.sql` é executado automaticamente quando o container PostgreSQL é criado pela primeira vez, criando os bancos de dados necessários.

### 2. Migrations Automáticas

Cada serviço tem suas próprias migrations na pasta `src/main/resources/db/migration/`:

#### Trip Service

- `V1__Create_trips_table.sql` - Criação da tabela de viagens
- `V2__Add_budget_and_status_to_trips.sql` - Exemplo de evolução do schema

#### Suggestion Service

- `V1__Create_suggestions_and_itineraries_tables.sql` - Criação das tabelas de sugestões e itinerários

### 3. Execução das Migrations

As migrations são executadas automaticamente quando os serviços iniciam, graças à configuração:

```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    baseline-version: 0
  jpa:
    hibernate:
      ddl-auto: validate # Flyway gerencia o schema, Hibernate apenas valida
```

## Como usar

### 1. Iniciar o ambiente

```bash
docker-compose up -d postgres
# Aguarde o PostgreSQL inicializar completamente
docker-compose up trip-service suggestion-service
```

### 2. Criar uma nova migration

#### Para Trip Service:

```bash
# Arquivo: trip-service/src/main/resources/db/migration/V3__Nome_da_migration.sql
```

#### Para Suggestion Service:

```bash
# Arquivo: suggestion-service/src/main/resources/db/migration/V2__Nome_da_migration.sql
```

### 3. Regras de nomenclatura

- **Formato**: `V{versão}__{descrição}.sql`
- **Versão**: Número incremental (V1, V2, V3...)
- **Descrição**: Snake_case separado por underscores
- **Exemplos**:
  - `V1__Create_users_table.sql`
  - `V2__Add_email_column_to_users.sql`
  - `V3__Create_index_on_user_email.sql`

### 4. Boas práticas

#### ✅ Faça

- Use sempre migrations incrementais
- Teste as migrations em ambiente de desenvolvimento primeiro
- Mantenha migrations simples e focadas
- Adicione comentários explicativos
- Use índices para colunas que serão consultadas frequentemente

#### ❌ Não faça

- Nunca modifique uma migration já aplicada em produção
- Não delete migrations já executadas
- Evite migrations que fazem rollback (Flyway é forward-only por padrão)

### 5. Comandos úteis

#### Verificar status das migrations

```bash
# Conectar ao container do serviço
docker exec -it trip-service-app bash
# Executar Flyway info (se disponível)
```

#### Limpar banco para recriação (apenas desenvolvimento)

```bash
docker-compose down -v  # Remove volumes (CUIDADO: apaga dados!)
docker-compose up
```

#### Verificar schema no PostgreSQL

```bash
docker exec -it postgres_db psql -U myuser -d trip_service_db
\dt  # Listar tabelas
\d trips  # Descrever tabela trips
```

## Resolução de problemas

### Migration falhou

1. Verifique os logs do serviço: `docker-compose logs trip-service`
2. Verifique se a sintaxe SQL está correta
3. Verifique se não há conflitos de versionamento

### Banco não foi criado

1. Verifique se o script `db-init/init.sql` está correto
2. Remova volumes e recrie: `docker-compose down -v && docker-compose up`

### Serviço não conecta ao banco

1. Verifique se o PostgreSQL iniciou completamente
2. Verifique as configurações de conexão no `application.yml`
3. Verifique se os bancos foram criados: `docker exec postgres_db psql -U myuser -l`
