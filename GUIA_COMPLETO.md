# Sistema de Curtidas Free Fire - Documentação

## 🎮 Visão Geral

API completa para gerenciamento de curtidas, Booyah Pass e informações de guilda para **Free Fire** nos servidores **africanos**.

## 📍 Regiões Suportadas

| Código | País | Moeda |
|--------|------|-------|
| **MZ** | Moçambique | USD |
| **AO** | Angola | USD |
| **CV** | Cabo Verde | USD |
| **GB** | Guiné-Bissau | USD |
| **ST** | São Tomé e Príncipe | USD |
| **GQ** | Guiné Equatorial | USD |

## 💰 Planos de Preço

### BÁSICO - $3.00
- 100 likes por dia
- Sem Booyah Pass
- Sem suporte prioritário

### PREMIUM - $4.19
- 500 likes por dia
- Booyah Pass incluído
- Sem suporte prioritário

### ELITE - $5.09
- Ilimitado likes por dia
- Booyah Pass ilimitado
- Suporte 24/7 prioritário

## 🚀 Arquitetura

```
┌─────────────────┐
│   Frontend      │ (Site/App)
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│  FastAPI        │ (Gateway - Port 3000)
│  Python 3.10    │
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│  Spring Boot    │ (Backend - Port 8080)
│  Java 17        │
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│  PostgreSQL     │ (Database - Port 5432)
│  Redis Cache    │ (Port 6379)
└─────────────────┘
```

## 🛠️ Stack Tecnológico

- **Backend**: Java 17 + Spring Boot 3.x
- **Gateway**: FastAPI + Python 3.10
- **Database**: PostgreSQL 15
- **Cache**: Redis 7
- **Autenticação**: JWT + OAuth2
- **Containerização**: Docker & Docker Compose

## 📥 Instalação

### Com Docker Compose (Recomendado)

```bash
# Clone o repositório
git clone https://github.com/darengggh-netizen/Sistema-de-curtidas-Free-Fire---Regi-o-Africana.git
cd Sistema-de-curtidas-Free-Fire---Regi-o-Africana

# Inicie todos os serviços
docker-compose up -d

# Aguarde 30 segundos para inicialização completa
sleep 30

# Acesse a API
open http://localhost:3000/docs
```

## 📚 Documentação da API

### Autenticação

#### Login
```bash
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "senha123",
  "country": "MZ"
}
```

**Response:**
```json
{
  "accessToken": "eyJ...",
  "refreshToken": "eyJ...",
  "email": "usuario@example.com",
  "username": "usuario",
  "plan": "PREMIUM",
  "country": "MZ",
  "apiKey": "usuario_abc123xyz..."
}
```

### Likes

#### Enviar Likes
```bash
POST /api/v1/likes/send?account_id=uuid
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "receiverFfAccountId": "12345",
  "quantity": 10,
  "plan": "PREMIUM"
}
```

#### Obter Meus Likes
```bash
GET /api/v1/likes/my-likes
Authorization: Bearer {accessToken}
```

#### Obter Likes Recebidos
```bash
GET /api/v1/likes/received/12345
```

### Guilda

#### Obter Info da Guilda
```bash
GET /api/v1/guilda/{guildId}
Authorization: Bearer {accessToken}
```

**Response:**
```json
{
  "id": "uuid",
  "guildId": "guild123",
  "guildName": "Minha Guilda",
  "level": 15,
  "totalMembers": 50,
  "leaderFfId": "leader123",
  "country": "MZ",
  "experiencePoints": 45000,
  "totalLikesReceived": 5000,
  "description": "Descrição da guilda",
  "createdAt": "2024-01-15T10:30:00"
}
```

### Booyah Pass

#### Enviar Booyah Pass
```bash
POST /api/v1/booyah/enviar?account_id=uuid
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "receiverFfAccountId": "12345",
  "quantity": 1
}
```

### Contas

#### Adicionar Conta Free Fire
```bash
POST /api/v1/account/add?ff_account_id=12345&ff_username=MeuUsername
Authorization: Bearer {accessToken}
```

#### Obter Info da Conta
```bash
GET /api/v1/account/{accountId}
```

#### Obter Data de Criação
```bash
GET /api/v1/account/info/{ffAccountId}
```

**Response:**
```json
{
  "accountCreatedDate": "2023-06-15T10:30:00"
}
```

### Planos de Preço

#### Obter Todos os Planos
```bash
GET /api/v1/pricing/plans
```

**Response:**
```json
[
  {
    "plan": "BASICO",
    "price": 3.00,
    "currency": "USD",
    "dailyLimit": 100,
    "features": {
      "likes_per_day": 100,
      "booyah_pass": false,
      "priority_support": false
    }
  },
  {
    "plan": "PREMIUM",
    "price": 4.19,
    "currency": "USD",
    "dailyLimit": 500,
    "features": {
      "likes_per_day": 500,
      "booyah_pass": true,
      "priority_support": false
    }
  },
  {
    "plan": "ELITE",
    "price": 5.09,
    "currency": "USD",
    "dailyLimit": -1,
    "features": {
      "likes_per_day": "Ilimitado",
      "booyah_pass": true,
      "priority_support": true
    }
  }
]
```

## 🔐 Segurança

✅ Autenticação JWT com tokens de acesso e refresh  
✅ Criptografia de senhas com bcrypt  
✅ Validação de IP por região  
✅ Rate limiting por plano  
✅ Chaves API criptografadas  
✅ CORS configurado  
✅ Logs de auditoria completos  
✅ Proteção contra SQL injection (ORM)  

## 🧪 Testes

```bash
# Executar testes do Gateway
cd gateway
pytest test_main.py -v

# Executar com cobertura
pytest test_main.py --cov=main --cov-report=html
```

## 📊 Monitoramento

- **Swagger UI**: http://localhost:3000/docs
- **ReDoc**: http://localhost:3000/redoc
- **Prometheus Metrics**: (em desenvolvimento)

## 🐛 Troubleshooting

### Erro: "Conexão recusada com backend"

```bash
# Verifique se o backend está rodando
docker-compose ps

# Reinicie os serviços
docker-compose restart backend
```

### Erro: "Porta já em uso"

```bash
# Libere a porta
sudo lsof -ti :3000,8080,5432 | xargs kill -9
```

## 📝 Configuração de Ambiente

Crie um arquivo `.env` baseado em `.env.example`:

```bash
cp gateway/.env.example gateway/.env
```

Edite com suas configurações:

```env
BACKEND_URL=http://localhost:8080
API_PORT=3000
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
```

## 📦 Deployment

### Em Produção

```bash
# Build das imagens
docker build -t ff-curtidas-backend ./backend
docker build -t ff-curtidas-gateway ./gateway

# Deploy com Docker Compose
docker-compose -f docker-compose.yml up -d

# Verificar status
docker-compose ps
```

## 📞 Suporte

Para dúvidas ou problemas:
- Email: darengggh@gmail.com
- Issues: https://github.com/darengggh-netizen/Sistema-de-curtidas-Free-Fire---Regi-o-Africana/issues

## 📄 Licença

Autorizado para uso comercial - Garena/SeaGame Group

## 🎯 Roadmap

- [ ] Integração com API real do Free Fire
- [ ] Sistema de pagamento Stripe/PayPal
- [ ] Dashboard administrativo
- [ ] Relatórios e análises
- [ ] App mobile nativa
- [ ] Múltiplos idiomas
- [ ] Sistema de referência
- [ ] Suporte a novos servidores

---

**Desenvolvido com ❤️ para a região Africana**
