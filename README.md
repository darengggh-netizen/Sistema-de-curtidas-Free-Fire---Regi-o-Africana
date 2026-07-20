# Sistema de Curtidas Free Fire - Região Africana 🎮

API completa para gerenciamento de curtidas, guilda e Booyah Pass para Free Fire nos servidores africanos.

## 📍 Regiões Suportadas

- 🇲🇿 Moçambique (MZ)
- 🇦🇴 Angola (AO)
- 🇨🇻 Cabo Verde (CV)
- 🇬🇼 Guiné-Bissau (GB)
- 🇸🇹 São Tomé e Príncipe (ST)
- 🇬🇶 Guiné Equatorial (GQ)

## 🎯 Funcionalidades

✅ **Autenticação Regional** - Por país/IP
✅ **Sistema de Likes** - Envio de curtidas em contas
✅ **Informações de Guilda** - Nível, membros, estatísticas
✅ **Booyah Pass** - Envio e gerenciamento de passes
✅ **Sistema de Recompensas** - USD 3.00 | USD 4.19 | USD 5.09
✅ **Data de Criação** - Histórico de contas
✅ **API Key Security** - Chaves criptografadas por região

## 💰 Planos de Preço

| Plano | Preço | Limite Diário |
|-------|-------|---------------|
| **Básico** | $3.00 | 100 likes/dia |
| **Premium** | $4.19 | 500 likes/dia |
| **Elite** | $5.09 | Ilimitado |

## 🛠️ Stack Tecnológico

- **Backend**: Java 17 + Spring Boot 3.x
- **API Gateway**: FastAPI (Python 3.10+)
- **Banco de Dados**: PostgreSQL 15
- **Autenticação**: JWT + OAuth2
- **Docker**: Containerização completa

## 🚀 Instalação Rápida

```bash
git clone https://github.com/darengggh-netizen/Sistema-de-curtidas-Free-Fire---Regi-o-Africana.git
cd Sistema-de-curtidas-Free-Fire---Regi-o-Africana
docker-compose up -d
```

## 📚 Endpoints Principais

- `POST /api/v1/auth/login` - Autenticação por país
- `POST /api/v1/likes/send` - Enviar likes
- `GET /api/v1/guilda/{guild_id}` - Info da guilda
- `POST /api/v1/booyah/enviar` - Enviar Booyah Pass
- `GET /api/v1/account/{account_id}/info` - Data criação + Info

## 🔐 Segurança

- JWT com refresh tokens
- Validação de IP por região
- Rate limiting por plano
- Criptografia de dados sensíveis
- Logs de auditoria completos
