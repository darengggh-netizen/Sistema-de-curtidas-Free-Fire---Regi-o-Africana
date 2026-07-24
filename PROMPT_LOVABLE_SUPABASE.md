# PROMPT PARA LOVABLE - SISTEMA DE CAMISAS FREE FIRE COM PERSISTÊNCIA

## 🎯 PROMPT PRINCIPAL PARA O LOVABLE

Crie um sistema completo de envio de camisas Free Fire com **autenticação JWT, armazenamento em banco de dados e persistência total de dados**.

---

## 📋 REQUISITOS TÉCNICOS

### 1. AUTENTICAÇÃO
- Login e Registro com email/senha
- JWT tokens com expiração de 24h
- Seleção obrigatória de país (MZ, AO, CV, GB, ST, GQ)
- Persistência de token no localStorage
- Logout com limpeza de dados

### 2. ARMAZENAMENTO DE DADOS

**Opção A - Supabase (RECOMENDADO)**
- Tabela `users`
- Tabela `shirts` 
- Tabela `transactions`
- Tabela `user_inventory`

**Opção B - Firebase**
- Autenticação Firebase Auth
- Firestore para dados

**Opção C - Backend Node.js + MongoDB**
- Express server
- MongoDB Atlas

### 3. DADOS A GUARDAR

**Usuário:**
```json
{
  "id": "UUID",
  "username": "string",
  "email": "string",
  "country": "string (MZ|AO|CV|GB|ST|GQ)",
  "password_hash": "string",
  "created_at": "timestamp",
  "updated_at": "timestamp",
  "total_shirts_sent": "number",
  "total_transactions": "number"
}
```

**Inventário (camisas do usuário):**
```json
{
  "id": "UUID",
  "user_id": "UUID",
  "shirt_id": "string (flamengo, vasco, etc)",
  "quantity": "number",
  "acquired_at": "timestamp"
}
```

**Transação (envio de camisa):**
```json
{
  "id": "UUID",
  "sender_id": "UUID",
  "sender_username": "string",
  "receiver_ff_account": "string",
  "shirt_id": "string",
  "shirt_name": "string",
  "quantity": "number",
  "timestamp": "timestamp",
  "status": "string (SUCCESS|PENDING|FAILED)"
}
```

### 4. ESTRUTURA DE CAMISAS (DADOS ESTÁTICOS)

```javascript
const SHIRTS = [
  // LEGENDARY
  { id: "cebolinha", name: "Camisa Cebolinha", team: "Cebolinha", rarity: "LEGENDARY", color: "Roxo", price: 200 },
  
  // EPIC
  { id: "flamengo", name: "Camisa do Flamengo", team: "Flamengo", rarity: "EPIC", color: "Vermelho e Preto", price: 100 },
  { id: "fluminense", name: "Camisa do Fluminense", team: "Fluminense", rarity: "EPIC", color: "Verde e Rosa", price: 100 },
  { id: "corinthians", name: "Camisa do Corinthians", team: "Corinthians", rarity: "EPIC", color: "Branco e Preto", price: 100 },
  { id: "palmeiras", name: "Camisa do Palmeiras", team: "Palmeiras", rarity: "EPIC", color: "Verde", price: 100 },
  
  // RARE
  { id: "botafogo", name: "Camisa do Botafogo", team: "Botafogo", rarity: "RARE", color: "Branco e Preto", price: 80 },
  { id: "vasco", name: "Camisa do Vasco", team: "Vasco da Gama", rarity: "RARE", color: "Branco e Preto", price: 80 },
  { id: "santos", name: "Camisa do Santos", team: "Santos FC", rarity: "RARE", color: "Branco", price: 80 },
  { id: "sao_paulo", name: "Camisa do São Paulo", team: "São Paulo FC", rarity: "RARE", color: "Vermelho, Branco e Preto", price: 80 },
  { id: "cruzeiro", name: "Camisa do Cruzeiro", team: "Cruzeiro", rarity: "RARE", color: "Azul", price: 80 },
  { id: "atletico_mg", name: "Camisa do Atlético MG", team: "Atlético Mineiro", rarity: "RARE", color: "Preto e Branco", price: 80 },
  { id: "gremio", name: "Camisa do Grêmio", team: "Grêmio", rarity: "RARE", color: "Azul", price: 80 },
  { id: "internacional", name: "Camisa do Internacional", team: "Internacional", rarity: "RARE", color: "Vermelho", price: 80 },
  
  // UNCOMMON
  { id: "bahia", name: "Camisa do Bahia", team: "Bahia", rarity: "UNCOMMON", color: "Azul e Branco", price: 60 },
  { id: "vitoria", name: "Camisa do Vitória", team: "Vitória", rarity: "UNCOMMON", color: "Vermelho", price: 60 },
  { id: "chapecoense", name: "Camisa da Chapecoense", team: "Chapecoense", rarity: "UNCOMMON", color: "Verde e Branco", price: 60 }
]
```

---

## 🏗️ ARQUITETURA RECOMENDADA

### Com Supabase (MAIS FÁCIL)

```
Frontend (React/Vue no Lovable)
         |
         ↓
    Supabase Client SDK
         |
         ↓
  Supabase (Backend as a Service)
         |
         ↓
   PostgreSQL Database
```

### Passos:

1. Criar projeto Supabase grátis (supabase.com)
2. Criar tabelas no Supabase
3. Configurar autenticação
4. Integrar SDK no Lovable
5. Pronto! Dados salvos automaticamente

---

## 📱 PÁGINAS NECESSÁRIAS

### 1. Landing Page (se não autenticado)
- Logo e título
- Explicação do serviço
- Botões "Login" e "Registrar"

### 2. Autenticação (Login/Registro)

**Login:**
- Email
- Senha
- Botão "Entrar"

**Registro:**
- Username
- Email
- Senha
- Confirmar senha
- Seletor de país (MZ, AO, CV, GB, ST, GQ)
- Botão "Criar Conta"

### 3. Dashboard Principal

**Header:**
- Logo
- Username do usuário logado
- País
- Botão Logout

**Content:**
- 3 abas: "Enviar Camisa", "Histórico", "Inventário"

#### Aba "Enviar Camisa":
- Grid com 16 camisas
- Cada camisa é um card com:
  - Nome da camisa
  - Time
  - Cor
  - Raridade (com badge colorido)
  - Preço
  - Botão "Selecionar"
- Forma de seleção:
  - ID da Conta Free Fire receptora (input)
  - Quantidade (select 1-100)
  - Botão "Enviar Camisa"
  - Status da transação (loading/sucesso/erro)

#### Aba "Histórico":
- Tabela com colunas:
  - Data/Hora
  - Camisa enviada
  - Para (conta receptora)
  - Quantidade
  - Status
- Ordenado por data (mais recente primeiro)
- Paginação (10 itens por página)

#### Aba "Inventário":
- Mostra camisas que o usuário tem
- Quantidade de cada
- Data que adquiriu
- Total de camisas

---

## 🔌 INTEGRAÇÕES NECESSÁRIAS

### Com Supabase:

```javascript
// No arquivo de configuração
import { createClient } from '@supabase/supabase-js'

const supabase = createClient(
  process.env.SUPABASE_URL,
  process.env.SUPABASE_ANON_KEY
)
```

### Variáveis de Ambiente:
```
VITE_SUPABASE_URL=https://xxxxx.supabase.co
VITE_SUPABASE_ANON_KEY=eyJhbGc...
```

---

## 💾 O QUE DEVE SER SALVO

✅ Dados de login (email, username, país)  
✅ Histórico completo de envios  
✅ Inventário (camisas adquiridas)  
✅ Timestamps de todas as ações  
✅ Status de cada transação  
✅ Token JWT com expiração  

---

## 🎨 DESIGN

**Cores:**
- Primária: #667eea (roxo azulado)
- Secundária: #764ba2 (roxo escuro)
- Destaque: #f97316 (laranja)
- Fundo: #f3f4f6 (cinza claro)
- Cards: white

**Raridades (cores de background):**
- LEGENDARY: #ff9800 (laranja)
- EPIC: #9c27b0 (roxo)
- RARE: #2196f3 (azul)
- UNCOMMON: #4caf50 (verde)

---

## ✨ FEATURES EXTRAS

- ✅ Dark mode toggle
- ✅ Toast notifications
- ✅ Loading spinners
- ✅ Confirmação antes de enviar
- ✅ Busca de camisas por nome
- ✅ Filtro por raridade
- ✅ Estatísticas do usuário
- ✅ Export de histórico (CSV)
- ✅ Responsivo (mobile/tablet/desktop)

---

## 📊 FLUXO DE DADOS

```
1. Usuário registra
   ↓
   Cria usuário no DB
   Gera JWT token
   Salva no localStorage

2. Usuário faz login
   ↓
   Valida credenciais
   Gera novo token
   Carrega dados do usuário

3. Usuário seleciona camisa e envia
   ↓
   Valida token
   Cria registro de transação no DB
   Atualiza inventário do usuário
   Incrementa contador de envios
   Retorna status sucesso

4. Usuário vê histórico
   ↓
   Busca transações no DB onde sender = user_id
   Exibe em tabela/cards
   Ordenado por data
```

---

## 🚀 PASSO A PASSO NO LOVABLE

1. **Cole este prompt no Lovable**

2. **Depois de gerar, peça ajustes:**
   - "Integre com Supabase para guardar dados"
   - "Crie tabelas users, shirts, transactions no Supabase"
   - "Implemente JWT com expiração de 24h"
   - "Adicione validações de form"
   - "Faça dark mode"

3. **Configure Supabase:**
   - Crie conta em supabase.com
   - Crie novo projeto
   - Copie URL e API Key
   - Cole no Lovable nas variáveis de ambiente

4. **Crie as tabelas no Supabase:**

```sql
-- Tabela Users
CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  username VARCHAR(100) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  country VARCHAR(2) NOT NULL,
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW(),
  total_shirts_sent INT DEFAULT 0,
  total_transactions INT DEFAULT 0
);

-- Tabela Transactions
CREATE TABLE transactions (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  sender_id UUID REFERENCES users(id),
  receiver_ff_account VARCHAR(100) NOT NULL,
  shirt_id VARCHAR(50) NOT NULL,
  quantity INT NOT NULL,
  status VARCHAR(20) DEFAULT 'SUCCESS',
  created_at TIMESTAMP DEFAULT NOW()
);

-- Tabela Inventory
CREATE TABLE inventory (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID REFERENCES users(id),
  shirt_id VARCHAR(50) NOT NULL,
  quantity INT DEFAULT 1,
  acquired_at TIMESTAMP DEFAULT NOW()
);
```

5. **Deploy:**
   - Lovable gera código automático
   - Deploy no Vercel/Netlify
   - Pronto! Tudo guardado!

---

## 📌 OBSERVAÇÕES IMPORTANTES

- **Supabase é gratuito** para uso pequeno
- **Lovable gera código automático** e integra tudo
- **Dados persistem** automaticamente no DB
- **Sem limite** de transações/histórico
- **Autenticação segura** com JWT

---

## 🎉 RESULTADO FINAL

Você terá um sistema COMPLETO que:
- ✅ Autentica usuários
- ✅ Salva TUDO no banco de dados
- ✅ Guarda histórico completo
- ✅ Mantém inventário atualizado
- ✅ Funciona offline (com sincronização)
- ✅ Escalável para milhares de usuários
- ✅ Seguro com JWT
- ✅ Pronto para produção

---

**Use este prompt no Lovable e terá seu sistema 100% funcional em minutos!** 🚀
