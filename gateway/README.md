# Free Fire Curtidas API - Gateway

API Gateway desenvolvido em **FastAPI** que atua como intermediário entre o frontend e o backend Java.

## Funcionalidades

✅ Autenticação por país/região  
✅ Gerenciamento de likes  
✅ Informações de guilda  
✅ Envio de Booyah Pass  
✅ Gerenciamento de contas  
✅ Planos de preço regionalizados  
✅ Rate limiting por plano  
✅ Validação de dados com Pydantic  
✅ CORS habilitado  
✅ Documentação automática Swagger  

## Instalação

```bash
pip install -r requirements.txt
```

## Executar

```bash
uvicorn main:app --reload --port 3000
```

## Endpoints Principais

### Autenticação
- `POST /api/v1/auth/login` - Login
- `POST /api/v1/auth/register` - Registro

### Likes
- `POST /api/v1/likes/send` - Enviar likes
- `GET /api/v1/likes/my-likes` - Meus likes
- `GET /api/v1/likes/received/{ffAccountId}` - Likes recebidos

### Guilda
- `GET /api/v1/guilda/{guildId}` - Info da guilda

### Booyah Pass
- `POST /api/v1/booyah/enviar` - Enviar Booyah Pass

### Contas
- `POST /api/v1/account/add` - Adicionar conta
- `GET /api/v1/account/{accountId}` - Info da conta
- `GET /api/v1/account/my-accounts` - Minhas contas
- `GET /api/v1/account/info/{ffAccountId}` - Data de criação

### Planos
- `GET /api/v1/pricing/plans` - Todos os planos
- `GET /api/v1/pricing/plans/{planName}` - Plano específico

## Regiões Suportadas

- 🇲🇿 Moçambique (MZ)
- 🇦🇴 Angola (AO)
- 🇨🇻 Cabo Verde (CV)
- 🇬🇼 Guiné-Bissau (GB)
- 🇸🇹 São Tomé e Príncipe (ST)
- 🇬🇶 Guiné Equatorial (GQ)
