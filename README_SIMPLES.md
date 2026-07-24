# SISTEMA SIMPLES DE ENVIO DE LIKES - FREE FIRE

## 🚀 Como Usar

### 1. Instalar dependências

```bash
pip install fastapi uvicorn
```

### 2. Rodar o backend

```bash
python backend_simples.py
```

O servidor vai rodar em `http://localhost:8000`

### 3. Abrir o frontend

Abra o arquivo `frontend_simples.html` no navegador.

## 📋 Funcionalidades

✅ Enviar likes para conta Free Fire  
✅ Histórico de envios  
✅ Estatísticas  
✅ Suporte a 6 países africanos  
✅ Persistência de dados (JSON)  
✅ Interface intuitiva  
✅ Validações  

## 🌍 Países Suportados

- 🇲🇿 Moçambique (MZ)
- 🇦🇴 Angola (AO)
- 🇨🇻 Cabo Verde (CV)
- 🇬🇼 Guiné-Bissau (GB)
- 🇸🇹 São Tomé e Príncipe (ST)
- 🇬🇶 Guiné Equatorial (GQ)

## 📡 Endpoints da API

### Enviar Like

```bash
POST http://localhost:8000/likes/send
Content-Type: application/json

{
  "sender_account": "123456789",
  "receiver_account": "987654321",
  "quantity": 100,
  "country": "MZ"
}
```

**Response:**
```json
{
  "success": true,
  "message": "100 likes enviados com sucesso!",
  "data": {
    "id": 1,
    "sender": "123456789",
    "receiver": "987654321",
    "quantity": 100,
    "country": "MZ",
    "timestamp": "2024-07-20T10:30:00.123456",
    "status": "SUCCESS"
  }
}
```

### Obter Histórico

```bash
GET http://localhost:8000/likes/history
```

**Com filtros:**

```bash
GET http://localhost:8000/likes/history?sender=123456789
GET http://localhost:8000/likes/history?receiver=987654321
```

### Obter Estatísticas

```bash
GET http://localhost:8000/likes/stats
```

**Response:**
```json
{
  "total_likes_sent": 5000,
  "total_transactions": 50,
  "average_per_transaction": 100
}
```

## 📁 Estrutura

```
.
├── backend_simples.py      # API FastAPI
├── frontend_simples.html   # Interface web
├── likes_history.json      # Dados salvos
└── README.md              # Este arquivo
```

## 🔒 Segurança

- ✅ Validação de inputs
- ✅ CORS habilitado
- ✅ Rate limiting simples (máx 10.000 likes por envio)
- ✅ Persistência de dados

## 💡 Próximos Passos

1. Integrar com banco de dados PostgreSQL
2. Adicionar autenticação JWT
3. Implementar rate limiting real
4. Adicionar integração com API real do Free Fire
5. Deploy em servidor

## 📞 Suporte

Para dúvidas ou erros, verifique:

1. Se o backend está rodando (`http://localhost:8000`)
2. Se o navegador permite CORS
3. Se os dados estão sendo salvos em `likes_history.json`

---

**Desenvolvido para a região Africana** 🌍
