from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List
from datetime import datetime
import json
import os

app = FastAPI(title="Free Fire Likes API")

# CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Data Models
class SendLikeRequest(BaseModel):
    sender_account: str
    receiver_account: str
    quantity: int
    country: str

class LikeHistory(BaseModel):
    id: int
    sender: str
    receiver: str
    quantity: int
    timestamp: str
    status: str

# In-memory storage
likes_history = []
like_id_counter = 1

# File for persistence
HISTORY_FILE = "likes_history.json"

def load_history():
    """Carrega histórico do arquivo"""
    global likes_history, like_id_counter
    if os.path.exists(HISTORY_FILE):
        with open(HISTORY_FILE, 'r') as f:
            data = json.load(f)
            likes_history = data.get('likes', [])
            like_id_counter = data.get('counter', 1)

def save_history():
    """Salva histórico no arquivo"""
    with open(HISTORY_FILE, 'w') as f:
        json.dump({
            'likes': likes_history,
            'counter': like_id_counter
        }, f, indent=2)

# Load on startup
load_history()

@app.on_event("shutdown")
async def shutdown():
    save_history()

@app.get("/")
async def root():
    return {
        "message": "Free Fire Likes API",
        "version": "1.0",
        "endpoints": {
            "send_like": "POST /likes/send",
            "history": "GET /likes/history",
            "health": "GET /health"
        }
    }

@app.get("/health")
async def health():
    return {"status": "healthy"}

@app.post("/likes/send")
async def send_like(request: SendLikeRequest):
    """
    Envia likes para uma conta Free Fire
    
    Params:
    - sender_account: ID da conta que envia
    - receiver_account: ID da conta que recebe
    - quantity: Quantidade de likes
    - country: País (MZ, AO, CV, GB, ST, GQ)
    """
    global like_id_counter
    
    # Validações
    if not sender_account:
        raise HTTPException(status_code=400, detail="sender_account é obrigatório")
    if not receiver_account:
        raise HTTPException(status_code=400, detail="receiver_account é obrigatório")
    if request.quantity <= 0:
        raise HTTPException(status_code=400, detail="quantity deve ser maior que 0")
    if request.quantity > 10000:
        raise HTTPException(status_code=400, detail="quantity máximo é 10000")
    
    # Criar registro
    like_record = {
        "id": like_id_counter,
        "sender": request.sender_account,
        "receiver": request.receiver_account,
        "quantity": request.quantity,
        "country": request.country,
        "timestamp": datetime.now().isoformat(),
        "status": "SUCCESS"
    }
    
    likes_history.append(like_record)
    like_id_counter += 1
    save_history()
    
    return {
        "success": True,
        "message": f"{request.quantity} likes enviados com sucesso!",
        "data": like_record
    }

@app.get("/likes/history")
async def get_history(sender: str = None, receiver: str = None):
    """
    Obtém histórico de likes
    
    Query params:
    - sender: Filtrar por conta que enviou (opcional)
    - receiver: Filtrar por conta que recebeu (opcional)
    """
    result = likes_history.copy()
    
    if sender:
        result = [l for l in result if l['sender'] == sender]
    
    if receiver:
        result = [l for l in result if l['receiver'] == receiver]
    
    return {
        "total": len(result),
        "likes": result
    }

@app.get("/likes/stats")
async def get_stats():
    """
    Obtém estatísticas de likes
    """
    total_likes = sum(l['quantity'] for l in likes_history)
    total_sent = len(likes_history)
    
    return {
        "total_likes_sent": total_likes,
        "total_transactions": total_sent,
        "average_per_transaction": round(total_likes / total_sent, 2) if total_sent > 0 else 0
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
