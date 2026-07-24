# SISTEMA DE ENVIO DE CAMISAS FREE FIRE

## 👕 Visão Geral

Sistema completo para enviar **camisas de times** do Free Fire com **autenticação JWT**.

## 🎯 Camisas Disponíveis

### Times Brasileiros

**Times de Elite (EPIC)** - $100
- 🔴 Flamengo (Vermelho e Preto)
- ✅ Fluminense (Verde e Rosa)
- 🎫 Corinthians (Branco e Preto)
- 🟯 Palmeiras (Verde)

**Times de Raridade (RARE)** - $80
- ⚪⚫ Botafogo (Branco e Preto)
- ⬜ Vasco da Gama (Branco e Preto)
- ⚪☑️ Santos (Branco)
- 🔘 São Paulo (Vermelho, Branco e Preto)
- 🔵 Cruzeiro (Azul)
- 🔵 Atlético Mineiro (Preto e Branco)
- 🔵 Grêmio (Azul)
- 🔴 Internacional (Vermelho)

**Times Comuns (UNCOMMON)** - $60
- 🔵 Bahia (Azul e Branco)
- 🔴 Vitória (Vermelho)
- 🝽️ Chapecoense (Verde e Branco)

**Especiais (LEGENDARY)** - $200
- 🟣 Cebolinha (Roxo) - Personagem especial

---

## 🚀 Instalação

### 1. Instalar dependências

```bash
pip install fastapi uvicorn pyjwt
```

### 2. Rodar o backend

```bash
python backend_shirts.py
```

Roda em: `http://localhost:8001`

### 3. Abrir no navegador

Abra o arquivo `shirts_system.html` no navegador.

---

## 📋 Funcionalidades

✅ Autenticação com JWT  
✅ Registro e Login de usuários  
✅ Envio de camisas com token  
✅ Histórico completo de envios  
✅ Perfil do usuário  
✅ Invéntario de camisas  
✅ Suporte a 6 países africanos  
✅ Sistema de raridade  
✅ Persistência de dados  

---

## 📡 API Endpoints

### Autenticação

**Registrar:**
```bash
POST http://localhost:8001/auth/register
Content-Type: application/json

{
  "username": "seu_usuario",
  "email": "seu@email.com",
  "password": "sua_senha",
  "country": "MZ"
}
```

**Login:**
```bash
POST http://localhost:8001/auth/login
Content-Type: application/json

{
  "email": "seu@email.com",
  "password": "sua_senha"
}
```

**Response:**
```json
{
  "access_token": "eyJ0eXAiOiJKV1QiLCJhbGc...",
  "token_type": "bearer",
  "user": {
    "email": "seu@email.com",
    "username": "seu_usuario",
    "country": "MZ"
  }
}
```

### Camisas

**Listar todas as camisas:**
```bash
GET http://localhost:8001/shirts/available
```

**Obter uma camisa específica:**
```bash
GET http://localhost:8001/shirts/flamengo
```

**Enviar camisa (COM TOKEN):**
```bash
POST http://localhost:8001/shirts/send
Authorization: Bearer {token}
Content-Type: application/json

{
  "receiver_ff_account": "123456789",
  "shirt": "flamengo",
  "quantity": 1
}
```

**Obter histórico (COM TOKEN):**
```bash
GET http://localhost:8001/shirts/history
Authorization: Bearer {token}
```

**Obter inventário (COM TOKEN):**
```bash
GET http://localhost:8001/shirts/inventory
Authorization: Bearer {token}
```

**Obter perfil (COM TOKEN):**
```bash
GET http://localhost:8001/user/profile
Authorization: Bearer {token}
```

---

## 📄 Estrutura de Dados

### Usuário
```json
{
  "username": "username",
  "email": "user@email.com",
  "password_hash": "hash_da_senha",
  "country": "MZ",
  "created_at": "2024-07-24T10:30:00",
  "shirts": [
    {
      "id": "flamengo",
      "name": "Camisa do Flamengo",
      "quantity": 5
    }
  ],
  "points": 0
}
```

### Transação
```json
{
  "id": 1,
  "sender": "username",
  "sender_email": "user@email.com",
  "receiver_ff_account": "123456789",
  "shirt_id": "flamengo",
  "shirt_name": "Camisa do Flamengo",
  "shirt_team": "Flamengo",
  "quantity": 1,
  "timestamp": "2024-07-24T10:30:00",
  "status": "SUCCESS"
}
```

---

## 🔐 Sistema de Tokens JWT

**Duração do token:** 24 horas

**Claims do token:**
- `email` - Email do usuário
- `username` - Nome de usuário
- `exp` - Tempo de expiração
- `iat` - Tempo de emissão

---

## 📊 Histórico de Envios

Cada envio de camisa cria um registro com:
- ID único
- Usuário que enviou
- Conta receptora
- Tipo de camisa
- Quantidade
- Data e hora
- Status (SUCCESS/FAILED)

---

## 💡 Tipos de Raridade

- **LEGENDARY** - Máximo ($200)
- **EPIC** - Muito raro ($100)
- **RARE** - Raro ($80)
- **UNCOMMON** - Comum ($60)

---

## 🌍 Países Suportados

- 🇲🇿 Moçambique (MZ)
- 🇦🇴 Angola (AO)
- 🇨🇻 Cabo Verde (CV)
- 🇬🇼 Guiné-Bissau (GB)
- 🇸🇹 São Tomé e Príncipe (ST)
- 🇬🇶 Guiné Equatorial (GQ)

---

## 📑 Exemplos de Uso

### Fluxo Completo

1. **Registrar novo usuário:**
   - Preencher username, email, senha, país
   - Sistema cria conta e retorna token JWT

2. **Login:**
   - Fornecer email e senha
   - Sistema retorna token para usar nas requisições

3. **Enviar camisa:**
   - Selecionar camisa desejada
   - Fornecer ID da conta receptora
   - Informar quantidade
   - Sistema envia e registra na transação

4. **Ver histórico:**
   - Sistema exibe todos os envios do usuário
   - Mostra detalhes de cada transação

---

## 📁 Arquivos de Dados

- `users_shirts.json` - Dados dos usuários
- `shirt_transactions.json` - Histórico de transações

---

## 🧪 Teste Rápido

### Com cURL

1. **Registrar:**
```bash
curl -X POST http://localhost:8001/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "teste",
    "email": "teste@email.com",
    "password": "senha123",
    "country": "MZ"
  }'
```

2. **Enviar camisa (com token):**
```bash
curl -X POST http://localhost:8001/shirts/send \
  -H "Authorization: Bearer {seu_token}" \
  -H "Content-Type: application/json" \
  -d '{
    "receiver_ff_account": "123456789",
    "shirt": "flamengo",
    "quantity": 1
  }'
```

---

## 📞 Suporte

Para dúvidas:
- Verifique se o backend está rodando na porta 8001
- Confirme que o token é válido
- Verifique os arquivos de dados JSON

---

**Desenvolvido para a região Africana** 🌍
