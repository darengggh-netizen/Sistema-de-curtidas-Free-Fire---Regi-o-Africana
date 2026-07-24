# GUIA COMPLETO - LOVABLE + SUPABASE

## 🎯 Como colocar no Lovable com persistência total

### Passo 1: Criar conta Supabase

1. Acesse: https://supabase.com
2. Clique "Start Your Project"
3. Conecte com GitHub ou Google
4. Crie novo projeto
5. Escolha região próxima
6. Aguarde criação (2-3 minutos)

### Passo 2: Pegar credenciais Supabase

1. Na dashboard do Supabase
2. Clique em "Settings" > "API"
3. Copie:
   - **Project URL** (vai em VITE_SUPABASE_URL)
   - **anon/public key** (vai em VITE_SUPABASE_ANON_KEY)

### Passo 3: Criar tabelas no Supabase

1. Na dashboard, clique em "SQL Editor"
2. Cole o SQL abaixo
3. Execute (botão "Run")

```sql
-- Criar tabelas
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

CREATE TABLE transactions (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  sender_id UUID REFERENCES users(id),
  receiver_ff_account VARCHAR(100) NOT NULL,
  shirt_id VARCHAR(50) NOT NULL,
  shirt_name VARCHAR(100),
  quantity INT NOT NULL,
  status VARCHAR(20) DEFAULT 'SUCCESS',
  created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE inventory (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID REFERENCES users(id),
  shirt_id VARCHAR(50) NOT NULL,
  quantity INT DEFAULT 1,
  acquired_at TIMESTAMP DEFAULT NOW()
);

-- Criar índices para melhor performance
CREATE INDEX idx_transactions_sender ON transactions(sender_id);
CREATE INDEX idx_inventory_user ON inventory(user_id);
```

### Passo 4: Ir para Lovable

1. Acesse: https://lovable.dev
2. Clique em "Create new"
3. Cole o prompt do arquivo `PROMPT_LOVABLE_SUPABASE.md`
4. Deixe o Lovable gerar

### Passo 5: Configurar variáveis de ambiente

1. No Lovable, vá em "Settings" > "Environment Variables"
2. Adicione:
   ```
   VITE_SUPABASE_URL=https://xxxxx.supabase.co
   VITE_SUPABASE_ANON_KEY=eyJhbGc...
   ```

### Passo 6: Testar

1. Clique "Preview" no Lovable
2. Registre uma conta
3. Envie uma camisa
4. Volte ao Supabase > "Table Editor"
5. Abra a tabela "transactions"
6. Veja se o registro apareceu ✅

---

## 🔄 O que é salvo automaticamente

✅ **Quando registrar:**
- Usuário salvo na tabela `users`
- Email, username, país, senha (criptografada)

✅ **Quando fazer login:**
- Sessão criada
- Token JWT gerado
- Dados carregados do DB

✅ **Quando enviar camisa:**
- Transação salva em `transactions`
- Inventário atualizado em `inventory`
- Contador de envios incrementado

✅ **Quando ver histórico:**
- Carregado do DB em tempo real

---

## 🚀 Deploy (opcional)

### No Lovable:
1. Clique "Deploy"
2. Escolha Vercel ou Netlify
3. Conecte sua conta
4. Deploy automático
5. Seu site ao vivo! 🎉

---

## 💡 Dicas

1. **Backup automático**: Supabase faz backup diário
2. **Limite grátis**: 500MB banco de dados
3. **Escalável**: Pague conforme cresce
4. **Seguro**: Criptografia automática
5. **API automática**: Supabase gera API REST automaticamente

---

## ❌ Se algo der errado

**"Erro de conexão Supabase"**
- Verificar variáveis de ambiente
- Verificar se URL e KEY estão certas
- Testar em modo anônimo

**"Erro ao salvar dados"**
- Verificar se tabelas foram criadas
- Verificar permissões de insert
- Ver logs no SQL Editor

**"Dados não aparecem"**
- Aguardar 2-3 segundos (sincronização)
- Recarregar página
- Ver em Table Editor do Supabase

---

## 📊 Monitorar dados

No Supabase:
- **Table Editor**: Ver/editar dados
- **SQL Editor**: Queries personalizadas
- **Logs**: Ver erros
- **Authentication**: Ver usuários registrados

---

**Pronto! Seu sistema está 100% funcional com persistência total!** 🎉
