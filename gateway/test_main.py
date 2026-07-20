import pytest
from fastapi.testclient import TestClient
from main import app

client = TestClient(app)

def test_health():
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json()["status"] == "healthy"

def test_pricing_plans():
    response = client.get("/api/v1/pricing/plans")
    assert response.status_code == 200
    plans = response.json()
    assert len(plans) == 3
    assert plans[0]["plan"] == "BASICO"
    assert plans[0]["price"] == 3.00
    assert plans[1]["plan"] == "PREMIUM"
    assert plans[1]["price"] == 4.19
    assert plans[2]["plan"] == "ELITE"
    assert plans[2]["price"] == 5.09

def test_regional_documentation():
    response = client.get("/api/v1/docs-regional")
    assert response.status_code == 200
    data = response.json()
    assert "regioes_suportadas" in data
    assert len(data["regioes_suportadas"]) == 6
