# Chatbot Gen

Sistema completo de gerenciamento de chatbots com autenticação, planos de assinatura, controle de limites e integração com WhatsApp via Evolution API.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias](#tecnologias)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Executando o Projeto](#executando-o-projeto)
- [API Documentation](#api-documentation)
- [Estrutura de Dados](#estrutura-de-dados)
- [Autenticação](#autenticação)
- [Desenvolvimento](#desenvolvimento)
- [Testes](#testes)
- [Deploy](#deploy)

## 🎯 Sobre o Projeto

Chatbot Gen é uma plataforma SaaS para criação e gerenciamento de chatbots com integração ao WhatsApp. O sistema oferece:

- **Autenticação e Autorização**: Sistema completo de autenticação JWT com roles (ADMIN e CLIENT)
- **Gerenciamento de Organizações**: Multi-tenant com suporte a múltiplas organizações
- **Planos de Assinatura**: Sistema flexível de planos com limites configuráveis
- **Controle de Limites**: Verificação automática de limites por plano (usuários, chatbots, números de telefone)
- **WebSocket**: Comunicação em tempo real para atualizações de status
- **Integração WhatsApp**: Preparado para integração com Evolution API

## 🛠 Tecnologias

### Backend

- **Java 25** - Linguagem de programação
- **Spring Boot 3.5.9** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Autenticação e autorização
- **Spring WebSocket** - Comunicação em tempo real
- **PostgreSQL 18** - Banco de dados relacional
- **Redis** - Cache e filas de mensagens
- **Flyway** - Migrations de banco de dados
- **JWT (JJWT)** - Autenticação baseada em tokens
- **MapStruct** - Mapeamento Entity ↔ DTO
- **Lombok** - Redução de boilerplate
- **SpringDoc OpenAPI** - Documentação Swagger/OpenAPI
- **OpenTelemetry** - Observabilidade e tracing distribuído
- **Gradle** - Gerenciador de dependências

### Frontend

- **Next.js 16** - Framework React full-stack
- **React 19** - Biblioteca UI
- **TypeScript** - Tipagem estática
- **Turbopack** - Bundler de alta performance
- **React Compiler** - Memoização automática
- **shadcn/ui** - Componentes UI
- **Tailwind CSS** - Estilização
- **TanStack Query** - Gerenciamento de estado servidor
- **Zustand** - Gerenciamento de estado cliente
- **React Hook Form** - Formulários
- **Zod** - Validação de schemas
- **lucide-react** - Ícones

## 📁 Estrutura do Projeto

```
chatbot-gen/
├── backend/                    # Aplicação Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/chatbotgen/aplicacao/
│   │   │   │   ├── Application.java          # Classe principal
│   │   │   │   ├── config/                   # Configurações
│   │   │   │   ├── controller/               # Controllers REST
│   │   │   │   ├── service/                  # Lógica de negócio
│   │   │   │   ├── repository/               # Repositories JPA
│   │   │   │   ├── model/                    # Entidades JPA
│   │   │   │   ├── dto/                      # Data Transfer Objects
│   │   │   │   ├── exception/                # Exceções customizadas
│   │   │   │   ├── websocket/                # Handlers WebSocket
│   │   │   │   └── util/                     # Utilitários
│   │   │   └── resources/
│   │   │       ├── application.properties    # Configurações
│   │   │       └── db/migration/             # Scripts Flyway
│   │   └── test/                              # Testes
│   ├── build.gradle                          # Dependências Gradle
│   └── settings.gradle
│
├── frontend/                   # Aplicação Next.js
│   ├── app/                   # App Router (Next.js 16)
│   │   ├── (auth)/            # Rotas de autenticação
│   │   ├── (dashboard)/       # Rotas do dashboard
│   │   └── layout.tsx         # Layout raiz
│   ├── components/            # Componentes React
│   │   ├── layout/            # Componentes de layout
│   │   ├── ui/                # Componentes shadcn/ui
│   │   └── plans/             # Componentes de planos
│   ├── hooks/                 # Custom hooks
│   │   └── queries/           # Hooks TanStack Query
│   ├── stores/                # Stores Zustand
│   ├── services/              # Serviços e APIs
│   ├── types/                 # Tipos TypeScript
│   ├── schemas/               # Schemas Zod
│   ├── providers/             # Providers React
│   └── package.json
│
└── README.md                  # Este arquivo
```

## 📦 Pré-requisitos

Antes de começar, você precisa ter instalado:

- **Java 25** ou superior
- **Node.js 20+** e **npm** ou **yarn**
- **PostgreSQL 18** ou superior
- **Redis** 7+ (opcional, mas recomendado)
- **Gradle 9.2+** (ou usar o wrapper incluído)

## 🚀 Instalação

### 1. Clone o repositório

```bash
git clone <url-do-repositorio>
cd chatbot-gen
```

### 2. Configure o Backend

```bash
cd backend
```

Crie um arquivo `.env.local` na raiz do diretório `backend/` com as seguintes variáveis:

```env
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=chatbotgen
DB_USER=postgres
DB_PASSWORD=postgres

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_DB=0
REDIS_PASSWORD=

# JWT
JWT_SECRET_KEY=sua-chave-secreta-jwt-aqui-minimo-256-bits

# Server
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=dev

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:3000

# OpenTelemetry (opcional)
OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4318/v1/traces
```

### 3. Configure o Frontend

```bash
cd frontend
npm install
# ou
yarn install
```

Crie um arquivo `.env.local` na raiz do diretório `frontend/`:

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

### 4. Configure o Banco de Dados

Crie o banco de dados PostgreSQL:

```sql
CREATE DATABASE chatbotgen;
```

As migrations do Flyway serão executadas automaticamente na primeira execução.

## ⚙️ Configuração

### Variáveis de Ambiente do Backend

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `DB_HOST` | Host do PostgreSQL | `localhost` |
| `DB_PORT` | Porta do PostgreSQL | `5432` |
| `DB_NAME` | Nome do banco de dados | `chatbotgen` |
| `DB_USER` | Usuário do PostgreSQL | `postgres` |
| `DB_PASSWORD` | Senha do PostgreSQL | `postgres` |
| `REDIS_HOST` | Host do Redis | `localhost` |
| `REDIS_PORT` | Porta do Redis | `6379` |
| `REDIS_DB` | Database do Redis | `0` |
| `REDIS_PASSWORD` | Senha do Redis | (vazio) |
| `JWT_SECRET_KEY` | Chave secreta para JWT | (obrigatório) |
| `JWT_ACCESS_TOKEN_EXPIRATION` | Expiração do access token (ms) | `3600000` (1h) |
| `JWT_REFRESH_TOKEN_EXPIRATION` | Expiração do refresh token (ms) | `604800000` (7d) |
| `SERVER_PORT` | Porta do servidor | `8080` |
| `SPRING_PROFILES_ACTIVE` | Profile ativo | `dev` |
| `CORS_ALLOWED_ORIGINS` | Origens permitidas CORS | `http://localhost:3000` |

### Variáveis de Ambiente do Frontend

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `NEXT_PUBLIC_API_URL` | URL da API backend | `http://localhost:8080` |

## 🏃 Executando o Projeto

### Backend

```bash
cd backend

# Usando Gradle Wrapper
./gradlew bootRun

# Ou usando Gradle instalado
gradle bootRun
```

O backend estará disponível em `http://localhost:8080`

### Frontend

```bash
cd frontend

# Desenvolvimento
npm run dev
# ou
yarn dev

# Build de produção
npm run build

# Executar build de produção
npm run start
```

O frontend estará disponível em `http://localhost:3000`

## 📚 API Documentation

Após iniciar o backend, a documentação da API estará disponível em:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

### Endpoints Principais

#### Autenticação
- `POST /api/v1/auth/login` - Login
- `POST /api/v1/auth/refresh` - Refresh token
- `POST /api/v1/auth/logout` - Logout

#### Usuários
- `GET /api/v1/users` - Listar usuários
- `GET /api/v1/users/{id}` - Obter usuário
- `POST /api/v1/users` - Criar usuário
- `PUT /api/v1/users/{id}` - Atualizar usuário
- `DELETE /api/v1/users/{id}` - Deletar usuário

#### Organizações
- `GET /api/v1/organizations` - Listar organizações
- `GET /api/v1/organizations/{id}` - Obter organização
- `POST /api/v1/organizations` - Criar organização
- `PUT /api/v1/organizations/{id}` - Atualizar organização
- `DELETE /api/v1/organizations/{id}` - Deletar organização

#### Planos
- `GET /api/v1/plans` - Listar planos
- `GET /api/v1/plans/{id}` - Obter plano
- `POST /api/v1/plans` - Criar plano (ADMIN)
- `PUT /api/v1/plans/{id}` - Atualizar plano (ADMIN)
- `DELETE /api/v1/plans/{id}` - Deletar plano (ADMIN)

#### Assinaturas
- `GET /api/v1/subscriptions` - Listar assinaturas
- `GET /api/v1/subscriptions/{id}` - Obter assinatura
- `POST /api/v1/subscriptions` - Criar assinatura
- `PUT /api/v1/subscriptions/{id}` - Atualizar assinatura
- `GET /api/v1/subscriptions/{id}/limits` - Obter limites da assinatura

## 🗄 Estrutura de Dados

### Entidades Principais

#### User
- `id` - Identificador único
- `name` - Nome completo
- `email` - Email (único)
- `password` - Senha (hash)
- `role` - Role (ADMIN ou CLIENT)
- `organization` - Organização associada
- `createdAt` - Data de criação
- `updatedAt` - Data de atualização

#### Organization
- `id` - Identificador único
- `name` - Nome da organização
- `document` - Documento (CNPJ/CPF)
- `createdAt` - Data de criação
- `updatedAt` - Data de atualização

#### Plan
- `id` - Identificador único
- `name` - Nome do plano
- `description` - Descrição
- `price` - Preço mensal
- `maxUsers` - Limite de usuários (-1 para ilimitado)
- `maxChatbots` - Limite de chatbots (-1 para ilimitado)
- `maxPhoneNumbers` - Limite de números de telefone (-1 para ilimitado)
- `active` - Se o plano está ativo
- `createdAt` - Data de criação
- `updatedAt` - Data de atualização

#### Subscription
- `id` - Identificador único
- `organization` - Organização
- `plan` - Plano
- `status` - Status (ACTIVE, INACTIVE, CANCELLED, EXPIRED)
- `startDate` - Data de início
- `endDate` - Data de término
- `createdAt` - Data de criação
- `updatedAt` - Data de atualização

## 🔐 Autenticação

O sistema utiliza autenticação JWT com dois tipos de tokens:

- **Access Token**: Token de curta duração (padrão: 1 hora)
- **Refresh Token**: Token de longa duração (padrão: 7 dias)

### Fluxo de Autenticação

1. **Login**: `POST /api/v1/auth/login`
   - Retorna `accessToken` e `refreshToken`

2. **Requisições Autenticadas**: Incluir header
   ```
   Authorization: Bearer <accessToken>
   ```

3. **Refresh Token**: `POST /api/v1/auth/refresh`
   - Quando o access token expirar, use o refresh token para obter um novo access token

### Roles

- **ADMIN**: Acesso completo à plataforma, pode gerenciar todos os recursos
- **CLIENT**: Acesso apenas aos recursos da própria organização

## 💻 Desenvolvimento

### Backend

#### Compilar o projeto

```bash
cd backend
./gradlew build
```

#### Executar testes

```bash
./gradlew test
```

#### Verificar estilo de código

```bash
./gradlew checkstyleMain
```

#### Gerar documentação

A documentação Swagger é gerada automaticamente. Acesse `http://localhost:8080/swagger-ui.html` após iniciar a aplicação.

### Frontend

#### Verificar tipos TypeScript

```bash
cd frontend
npm run typecheck
```

#### Executar lint

```bash
npm run lint
```

#### Build de produção

```bash
npm run build
```

## 🧪 Testes

### Backend

O projeto utiliza **Testcontainers** para testes de integração com PostgreSQL real:

```bash
cd backend
./gradlew test
```

### Frontend

Testes do frontend podem ser adicionados conforme necessário.

## 🚢 Deploy

### Backend

1. **Build do JAR**:
   ```bash
   cd backend
   ./gradlew bootJar
   ```

2. **Executar JAR**:
   ```bash
   java -jar build/libs/backend-0.1.0.jar
   ```

3. **Variáveis de Ambiente**: Configure todas as variáveis de ambiente necessárias no ambiente de produção.

### Frontend

1. **Build**:
   ```bash
   cd frontend
   npm run build
   ```

2. **Executar**:
   ```bash
   npm run start
   ```

3. **Deploy na Vercel** (recomendado):
   - Conecte o repositório à Vercel
   - Configure as variáveis de ambiente
   - O deploy será automático

## 📝 Notas Adicionais

### Virtual Threads (Java 25)

O projeto utiliza Virtual Threads do Java 25 para melhor performance em operações I/O bound:

```properties
spring.threads.virtual.enabled=true
```

### Observabilidade

O projeto está configurado com OpenTelemetry para tracing distribuído. Configure o endpoint OTLP conforme necessário.

### Migrations

As migrations do Flyway são executadas automaticamente na inicialização. Certifique-se de que o banco de dados está acessível antes de iniciar a aplicação.

## 📄 Licença

Este projeto é privado e proprietário.

## 👥 Contribuindo

Este é um projeto privado. Para contribuições, entre em contato com a equipe de desenvolvimento.

---

**Desenvolvido com ❤️ usando Spring Boot e Next.js**

