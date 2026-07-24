from fastapi import FastAPI, HTTPException, Header, Depends
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, EmailStr
from typing import List, Optional
from datetime import datetime, timedelta
import json
import os
import jwt
import hashlib
from enum import Enum

app = FastAPI(title="Free Fire Shirts API")

# CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Configurações
SECRET_KEY = "free-fire-shirts-secret-key-2024"
ALGORITHM = "HS256"
TOKEN_EXPIRATION_HOURS = 24

# Files
USERS_FILE = "users_shirts.json"
SHIRTS_FILE = "shirts_data.json"
TRANSACTIONS_FILE = "shirt_transactions.json"

# Enums - Times Brasileiros
class TeamShirt(str, Enum):
    FLAMENGO = "flamengo"
    VASCO = "vasco"
    BOTAFOGO = "botafogo"
    FLUMINENSE = "fluminense"
    SANTOS = "santos"
    CORINTHIANS = "corinthians"
    SAO_PAULO = "sao_paulo"
    PALMEIRAS = "palmeiras"
    CRUZEIRO = "cruzeiro"
    ATLETICO_MG = "atletico_mg"
    GREMIO = "gremio"
    INTERNACIONAL = "internacional"
    BAHIA = "bahia"
    VITORIA = "vitoria"
    CEBOLINHA = "cebolinha"
    CHAPECOENSE = "chapecoense"

class Country(str, Enum):
    MZ = "MZ"
    AO = "AO"
    CV = "CV"
    GB = "GB"
    ST = "ST"
    GQ = "GQ"

# Data Models
class RegisterRequest(BaseModel):
    email: EmailStr
    password: str
    username: str
    country: Country

class LoginRequest(BaseModel):
    email: str
    password: str

class SendShirtRequest(BaseModel):
    receiver_ff_account: str
    shirt: TeamShirt
    quantity: int = 1

class TokenResponse(BaseModel):
    access_token: str
    token_type: str
    user: dict

class ShirtInfo(BaseModel):
    id: str
    name: str
    team: str
    rarity: str
    description: str

class TransactionResponse(BaseModel):
    id: int
    sender: str
    receiver: str
    shirt: str
    quantity: int
    timestamp: str
    status: str

# Dados de camisas
SHIRTS_DATA = {
    "flamengo": {
        "name": "Camisa do Flamengo",
        "team": "Flamengo",
        "rarity": "EPIC",
        "color": "Vermelho e Preto",
        "description": "Camisa oficial do Flamengo",
        "price": 100
    },
    "vasco": {
        "name": "Camisa do Vasco",
        "team": "Vasco da Gama",
        "rarity": "EPIC",
        "color": "Branco e Preto",
        "description": "Camisa oficial do Vasco da Gama",
        "price": 100
    },
    "botafogo": {
        "name": "Camisa do Botafogo",
        "team": "Botafogo",
        "rarity": "RARE",
        "color": "Branco e Preto",
        "description": "Camisa oficial do Botafogo",
        "price": 80
    },
    "fluminense": {
        "name": "Camisa do Fluminense",
        "team": "Fluminense",
        "rarity": "EPIC",
        "color": "Verde e Rosa",
        "description": "Camisa oficial do Fluminense",
        "price": 100
    },
    "santos": {
        "name": "Camisa do Santos",
        "team": "Santos FC",
        "rarity": "RARE",
        "color": "Branco",
        "description": "Camisa oficial do Santos FC",
        "price": 80
    },
    "corinthians": {
        "name": "Camisa do Corinthians",
        "team": "Corinthians",
        "rarity": "EPIC",
        "color": "Branco e Preto",
        "description": "Camisa oficial do Corinthians",
        "price": 100
    },
    "sao_paulo": {
        "name": "Camisa do São Paulo",
        "team": "São Paulo FC",
        "rarity": "RARE",
        "color": "Vermelho, Branco e Preto",
        "description": "Camisa oficial do São Paulo FC",
        "price": 80
    },
    "palmeiras": {
        "name": "Camisa do Palmeiras",
        "team": "Palmeiras",
        "rarity": "EPIC",
        "color": "Verde",
        "description": "Camisa oficial do Palmeiras",
        "price": 100
    },
    "cruzeiro": {
        "name": "Camisa do Cruzeiro",
        "team": "Cruzeiro",
        "rarity": "RARE",
        "color": "Azul",
        "description": "Camisa oficial do Cruzeiro",
        "price": 80
    },
    "atletico_mg": {
        "name": "Camisa do Atlético Mineiro",
        "team": "Atlético Mineiro",
        "rarity": "RARE",
        "color": "Preto e Branco",
        "description": "Camisa oficial do Atlético Mineiro",
        "price": 80
    },
    "gremio": {
        "name": "Camisa do Grêmio",
        "team": "Grêmio",
        "rarity": "RARE",
        "color": "Azul",
        "description": "Camisa oficial do Grêmio",
        "price": 80
    },
    "internacional": {
        "name": "Camisa do Internacional",
        "team": "Internacional",
        "rarity": "RARE",
        "color": "Vermelho",
        "description": "Camisa oficial do Internacional",
        "price": 80
    },
    "bahia": {
        "name": "Camisa do Bahia",
        "team": "Bahia",
        "rarity": "UNCOMMON",
        "color": "Azul e Branco",
        "description": "Camisa oficial do Bahia",
        "price": 60
    },
    "vitoria": {
        "name": "Camisa do Vitória",
        "team": "Vitória",
        "rarity": "UNCOMMON",
        "color": "Vermelho",
        "description": "Camisa oficial do Vitória",
        "price": 60
    },
    "cebolinha": {
        "name": "Camisa Cebolinha",
        "team": "Cebolinha",
        "rarity": "LEGENDARY",
        "color": "Roxo",
        "description": "Camisa especial do personagem Cebolinha",
        "price": 200
    },
    "chapecoense": {
        "name": "Camisa da Chapecoense",
        "team": "Chapecoense",
        "rarity": "UNCOMMON",
        "color": "Verde e Branco",
        "description": "Camisa oficial da Chapecoense",
        "price": 60
    }
}

# In-memory storage
users_db = {}
transactions = []
transaction_id_counter = 1

def load_data():
    global users_db, transactions, transaction_id_counter
    if os.path.exists(USERS_FILE):
        with open(USERS_FILE, 'r') as f:
            users_db = json.load(f)
    if os.path.exists(TRANSACTIONS_FILE):
        with open(TRANSACTIONS_FILE, 'r') as f:
            data = json.load(f)
            transactions = data.get('transactions', [])
            transaction_id_counter = data.get('counter', 1)

def save_data():
    with open(USERS_FILE, 'w') as f:
        json.dump(users_db, f, indent=2)
    with open(TRANSACTIONS_FILE, 'w') as f:
        json.dump({
            'transactions': transactions,
            'counter': transaction_id_counter
        }, f, indent=2)

load_data()

def hash_password(password: str) -> str:
    return hashlib.sha256(password.encode()).hexdigest()

def create_token(user_email: str, username: str) -> str:
    payload = {
        'email': user_email,
        'username': username,
        'exp': datetime.utcnow() + timedelta(hours=TOKEN_EXPIRATION_HOURS),
        'iat': datetime.utcnow()
    }
    return jwt.encode(payload, SECRET_KEY, algorithm=ALGORITHM)

def verify_token(token: str) -> dict:
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        return payload
    except jwt.ExpiredSignatureError:
        raise HTTPException(status_code=401, detail="Token expirado")
    except jwt.InvalidTokenError:
        raise HTTPException(status_code=401, detail="Token inválido")

def get_current_user(authorization: str = Header(None)) -> dict:
    if not authorization:
        raise HTTPException(status_code=401, detail="Token não fornecido")
    
    try:
        token = authorization.replace("Bearer ", "")
        return verify_token(token)
    except HTTPException:
        raise

@app.on_event("shutdown")
async def shutdown():
    save_data()

# Health Check
@app.get("/health")
async def health():
    return {"status": "healthy", "service": "Free Fire Shirts API"}

# Auth Endpoints
@app.post("/auth/register", response_model=TokenResponse)
async def register(request: RegisterRequest):
    if request.email in users_db:
        raise HTTPException(status_code=400, detail="Email já registrado")
    
    user_data = {
        "username": request.username,
        "email": request.email,
        "password_hash": hash_password(request.password),
        "country": request.country.value,
        "created_at": datetime.now().isoformat(),
        "shirts": [],
        "points": 0
    }
    
    users_db[request.email] = user_data
    save_data()
    
    token = create_token(request.email, request.username)
    
    return {
        "access_token": token,
        "token_type": "bearer",
        "user": {
            "email": request.email,
            "username": request.username,
            "country": request.country.value
        }
    }

@app.post("/auth/login", response_model=TokenResponse)
async def login(request: LoginRequest):
    if request.email not in users_db:
        raise HTTPException(status_code=401, detail="Email ou senha incorretos")
    
    user = users_db[request.email]
    if user["password_hash"] != hash_password(request.password):
        raise HTTPException(status_code=401, detail="Email ou senha incorretos")
    
    token = create_token(request.email, user["username"])
    
    return {
        "access_token": token,
        "token_type": "bearer",
        "user": {
            "email": request.email,
            "username": user["username"],
            "country": user["country"]
        }
    }

# Shirts Endpoints
@app.get("/shirts/available")
async def get_available_shirts():
    return {
        "total": len(SHIRTS_DATA),
        "shirts": [
            {
                "id": shirt_id,
                **shirt_data
            }
            for shirt_id, shirt_data in SHIRTS_DATA.items()
        ]
    }

@app.get("/shirts/{shirt_id}")
async def get_shirt(shirt_id: str):
    if shirt_id not in SHIRTS_DATA:
        raise HTTPException(status_code=404, detail="Camisa não encontrada")
    
    return {
        "id": shirt_id,
        **SHIRTS_DATA[shirt_id]
    }

@app.post("/shirts/send")
async def send_shirt(
    request: SendShirtRequest,
    current_user: dict = Depends(get_current_user)
):
    global transaction_id_counter
    
    # Validações
    if request.shirt.value not in SHIRTS_DATA:
        raise HTTPException(status_code=404, detail="Camisa não encontrada")
    
    if request.quantity <= 0:
        raise HTTPException(status_code=400, detail="Quantidade deve ser maior que 0")
    
    if request.quantity > 100:
        raise HTTPException(status_code=400, detail="Máximo de 100 camisas por envio")
    
    shirt_info = SHIRTS_DATA[request.shirt.value]
    
    # Criar transação
    transaction = {
        "id": transaction_id_counter,
        "sender": current_user["username"],
        "sender_email": current_user["email"],
        "receiver_ff_account": request.receiver_ff_account,
        "shirt_id": request.shirt.value,
        "shirt_name": shirt_info["name"],
        "shirt_team": shirt_info["team"],
        "quantity": request.quantity,
        "timestamp": datetime.now().isoformat(),
        "status": "SUCCESS"
    }
    
    transactions.append(transaction)
    transaction_id_counter += 1
    
    # Adicionar camisa ao invéntario do usuário
    if current_user["email"] in users_db:
        user = users_db[current_user["email"]]
        shirt_exists = False
        for shirt in user["shirts"]:
            if shirt["id"] == request.shirt.value:
                shirt["quantity"] += request.quantity
                shirt_exists = True
                break
        
        if not shirt_exists:
            user["shirts"].append({
                "id": request.shirt.value,
                "name": shirt_info["name"],
                "quantity": request.quantity
            })
        
        save_data()
    
    return {
        "success": True,
        "message": f"{request.quantity}x {shirt_info['name']} enviada com sucesso!",
        "transaction": transaction
    }

@app.get("/shirts/history")
async def get_shirt_history(current_user: dict = Depends(get_current_user)):
    user_transactions = [
        t for t in transactions 
        if t["sender_email"] == current_user["email"]
    ]
    return {
        "total": len(user_transactions),
        "transactions": user_transactions
    }

@app.get("/shirts/inventory")
async def get_inventory(current_user: dict = Depends(get_current_user)):
    if current_user["email"] not in users_db:
        raise HTTPException(status_code=404, detail="Usuário não encontrado")
    
    user = users_db[current_user["email"]]
    return {
        "username": user["username"],
        "total_shirts": len(user["shirts"]),
        "shirts": user["shirts"]
    }

@app.get("/user/profile")
async def get_profile(current_user: dict = Depends(get_current_user)):
    if current_user["email"] not in users_db:
        raise HTTPException(status_code=404, detail="Usuário não encontrado")
    
    user = users_db[current_user["email"]]
    user_transactions = [t for t in transactions if t["sender_email"] == current_user["email"]]
    
    return {
        "email": current_user["email"],
        "username": user["username"],
        "country": user["country"],
        "created_at": user["created_at"],
        "total_shirts": len(user["shirts"]),
        "total_sent": len(user_transactions),
        "total_items_sent": sum(t["quantity"] for t in user_transactions)
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8001)
