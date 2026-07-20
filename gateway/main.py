from fastapi import FastAPI, HTTPException, Depends, Header, Request
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, EmailStr, validator
from typing import Optional, List
from enum import Enum
import httpx
import os
from dotenv import load_dotenv
import logging

load_dotenv()

# Configuração de logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = FastAPI(
    title="Free Fire Curtidas API Gateway",
    description="API Gateway para Sistema de Curtidas Free Fire - Região Africana",
    version="1.0.0"
)

# CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Configurações
BACKEND_URL = os.getenv("BACKEND_URL", "http://backend:8080")
API_PORT = int(os.getenv("API_PORT", 3000))

# Enums
class CountryCode(str, Enum):
    MZ = "MZ"  # Moçambique
    AO = "AO"  # Angola
    CV = "CV"  # Cabo Verde
    GB = "GB"  # Guiné-Bissau
    ST = "ST"  # São Tomé e Príncipe
    GQ = "GQ"  # Guiné Equatorial

class PlanType(str, Enum):
    BASICO = "BASICO"
    PREMIUM = "PREMIUM"
    ELITE = "ELITE"

# DTOs
class LoginRequest(BaseModel):
    email: EmailStr
    password: str
    country: CountryCode

    @validator('password')
    def password_length(cls, v):
        if len(v) < 6:
            raise ValueError('Senha deve ter no mínimo 6 caracteres')
        return v

class LoginResponse(BaseModel):
    accessToken: str
    refreshToken: str
    email: str
    username: str
    plan: str
    country: str
    apiKey: str

class LikeRequest(BaseModel):
    receiverFfAccountId: str
    quantity: int
    plan: PlanType

    @validator('quantity')
    def quantity_positive(cls, v):
        if v <= 0:
            raise ValueError('Quantidade deve ser maior que 0')
        return v

class LikeResponse(BaseModel):
    id: str
    senderUsername: str
    receiverFfAccountId: str
    quantity: int
    plan: str
    status: str
    responseMessage: Optional[str]
    createdAt: str

class GuildInfoResponse(BaseModel):
    id: str
    guildId: str
    guildName: str
    level: int
    totalMembers: int
    leaderFfId: str
    country: Optional[str]
    experiencePoints: int
    totalLikesReceived: int
    description: Optional[str]
    createdAt: str

class BooyahPassRequest(BaseModel):
    receiverFfAccountId: str
    quantity: int = 1

class AccountInfoResponse(BaseModel):
    id: str
    ffAccountId: str
    ffUsername: str
    level: int
    experience: int
    accountCreatedDate: str
    lastSynced: str

class PricingPlan(BaseModel):
    plan: str
    price: float
    currency: str
    dailyLimit: int
    features: dict

# Dados de pricing (será sincronizado com BD)
PRICING_PLANS = [
    {
        "plan": "BASICO",
        "price": 3.00,
        "currency": "USD",
        "dailyLimit": 100,
        "features": {
            "likes_per_day": 100,
            "booyah_pass": False,
            "priority_support": False
        }
    },
    {
        "plan": "PREMIUM",
        "price": 4.19,
        "currency": "USD",
        "dailyLimit": 500,
        "features": {
            "likes_per_day": 500,
            "booyah_pass": True,
            "priority_support": False
        }
    },
    {
        "plan": "ELITE",
        "price": 5.09,
        "currency": "USD",
        "dailyLimit": -1,
        "features": {
            "likes_per_day": "Ilimitado",
            "booyah_pass": True,
            "priority_support": True
        }
    }
]

# Helper functions
async def make_backend_request(method: str, endpoint: str, data: Optional[dict] = None, token: Optional[str] = None):
    """Faz requisição para backend Java"""
    url = f"{BACKEND_URL}{endpoint}"
    headers = {"Content-Type": "application/json"}
    
    if token:
        headers["Authorization"] = f"Bearer {token}"
    
    async with httpx.AsyncClient() as client:
        try:
            if method == "POST":
                response = await client.post(url, json=data, headers=headers, timeout=30.0)
            elif method == "GET":
                response = await client.get(url, headers=headers, timeout=30.0)
            else:
                raise ValueError(f"Método HTTP {method} não suportado")
            
            response.raise_for_status()
            return response.json()
        except httpx.HTTPError as e:
            logger.error(f"Erro ao comunicar com backend: {e}")
            raise HTTPException(status_code=500, detail="Erro ao comunicar com o servidor")

# Health Check
@app.get("/health")
async def health():
    return {"status": "healthy", "service": "Free Fire Curtidas API Gateway"}

# Authentication Endpoints
@app.post("/api/v1/auth/login", response_model=LoginResponse)
async def login(request: LoginRequest):
    """
    Autentica usuário com email, senha e país
    """
    result = await make_backend_request(
        "POST",
        "/api/v1/auth/login",
        {
            "email": request.email,
            "password": request.password,
            "country": request.country.value
        }
    )
    return result

@app.post("/api/v1/auth/register", response_model=LoginResponse)
async def register(request: LoginRequest, username: str):
    """
    Registra novo usuário
    """
    result = await make_backend_request(
        "POST",
        f"/api/v1/auth/register?username={username}",
        {
            "email": request.email,
            "password": request.password,
            "country": request.country.value
        }
    )
    return result

# Likes Endpoints
@app.post("/api/v1/likes/send", response_model=LikeResponse)
async def send_like(
    account_id: str,
    request: LikeRequest,
    authorization: str = Header(None)
):
    """
    Envia likes para uma conta Free Fire
    """
    if not authorization:
        raise HTTPException(status_code=401, detail="Token não fornecido")
    
    token = authorization.replace("Bearer ", "")
    result = await make_backend_request(
        "POST",
        f"/api/v1/likes/send?accountId={account_id}",
        {
            "receiverFfAccountId": request.receiverFfAccountId,
            "quantity": request.quantity,
            "plan": request.plan.value
        },
        token
    )
    return result

@app.get("/api/v1/likes/my-likes", response_model=List[LikeResponse])
async def get_my_likes(authorization: str = Header(None)):
    """
    Obtém likes enviados pelo usuário
    """
    if not authorization:
        raise HTTPException(status_code=401, detail="Token não fornecido")
    
    token = authorization.replace("Bearer ", "")
    result = await make_backend_request("GET", "/api/v1/likes/my-likes", token=token)
    return result

@app.get("/api/v1/likes/received/{ff_account_id}", response_model=List[LikeResponse])
async def get_received_likes(ff_account_id: str):
    """
    Obtém likes recebidos por uma conta
    """
    result = await make_backend_request("GET", f"/api/v1/likes/received/{ff_account_id}")
    return result

# Guild Endpoints
@app.get("/api/v1/guilda/{guild_id}", response_model=GuildInfoResponse)
async def get_guild_info(guild_id: str, authorization: str = Header(None)):
    """
    Obtém informações de uma guilda
    """
    if not authorization:
        raise HTTPException(status_code=401, detail="Token não fornecido")
    
    token = authorization.replace("Bearer ", "")
    result = await make_backend_request("GET", f"/api/v1/guilda/{guild_id}", token=token)
    return result

# Booyah Pass Endpoints
@app.post("/api/v1/booyah/enviar")
async def send_booyah_pass(
    account_id: str,
    request: BooyahPassRequest,
    authorization: str = Header(None)
):
    """
    Envia Booyah Pass para um jogador
    """
    if not authorization:
        raise HTTPException(status_code=401, detail="Token não fornecido")
    
    token = authorization.replace("Bearer ", "")
    result = await make_backend_request(
        "POST",
        f"/api/v1/booyah/enviar?accountId={account_id}&receiverFfAccountId={request.receiverFfAccountId}&quantity={request.quantity}",
        {},
        token
    )
    return result

# Account Endpoints
@app.post("/api/v1/account/add", response_model=AccountInfoResponse)
async def add_account(
    ff_account_id: str,
    ff_username: str,
    authorization: str = Header(None)
):
    """
    Adiciona uma conta Free Fire ao usuário
    """
    if not authorization:
        raise HTTPException(status_code=401, detail="Token não fornecido")
    
    token = authorization.replace("Bearer ", "")
    result = await make_backend_request(
        "POST",
        f"/api/v1/account/add?ffAccountId={ff_account_id}&ffUsername={ff_username}",
        {},
        token
    )
    return result

@app.get("/api/v1/account/{account_id}", response_model=AccountInfoResponse)
async def get_account_info(account_id: str):
    """
    Obtém informações de uma conta Free Fire
    """
    result = await make_backend_request("GET", f"/api/v1/account/{account_id}")
    return result

@app.get("/api/v1/account/my-accounts")
async def get_my_accounts(authorization: str = Header(None)):
    """
    Obtém todas as contas Free Fire do usuário
    """
    if not authorization:
        raise HTTPException(status_code=401, detail="Token não fornecido")
    
    token = authorization.replace("Bearer ", "")
    result = await make_backend_request("GET", "/api/v1/account/my-accounts", token=token)
    return result

@app.get("/api/v1/account/info/{ff_account_id}")
async def get_account_creation_date(ff_account_id: str):
    """
    Obtém a data de criação de uma conta Free Fire
    """
    result = await make_backend_request("GET", f"/api/v1/account/info/{ff_account_id}")
    return result

# Pricing Endpoints
@app.get("/api/v1/pricing/plans", response_model=List[PricingPlan])
async def get_pricing_plans():
    """
    Obtém todos os planos de preço disponíveis
    Regiões: Moçambique, Angola, Cabo Verde, Guiné-Bissau, São Tomé e Príncipe, Guiné Equatorial
    """
    return PRICING_PLANS

@app.get("/api/v1/pricing/plans/{plan_name}", response_model=PricingPlan)
async def get_pricing_plan(plan_name: PlanType):
    """
    Obtém informações de um plano específico
    """
    for plan in PRICING_PLANS:
        if plan["plan"] == plan_name.value:
            return plan
    raise HTTPException(status_code=404, detail="Plano não encontrado")

# Documentation
@app.get("/api/v1/docs-regional")
async def get_regional_documentation():
    """
    Documentação sobre as regiões suportadas
    """
    return {
        "regioes_suportadas": [
            {"codigo": "MZ", "pais": "Moçambique", "moeda": "USD"},
            {"codigo": "AO", "pais": "Angola", "moeda": "USD"},
            {"codigo": "CV", "pais": "Cabo Verde", "moeda": "USD"},
            {"codigo": "GB", "pais": "Guiné-Bissau", "moeda": "USD"},
            {"codigo": "ST", "pais": "São Tomé e Príncipe", "moeda": "USD"},
            {"codigo": "GQ", "pais": "Guiné Equatorial", "moeda": "USD"}
        ],
        "planos_disponiveis": PRICING_PLANS,
        "descricao": "API para gerenciamento de curtidas, Booyah Pass e informações de guilda para Free Fire na região Africana"
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=API_PORT)
