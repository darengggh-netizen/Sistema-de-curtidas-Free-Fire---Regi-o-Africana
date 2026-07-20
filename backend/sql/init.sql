-- Create Extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Enum Types
CREATE TYPE plan_type AS ENUM ('BASICO', 'PREMIUM', 'ELITE');
CREATE TYPE transaction_status AS ENUM ('PENDING', 'SUCCESS', 'FAILED', 'CANCELLED');
CREATE TYPE country_code AS ENUM ('MZ', 'AO', 'CV', 'GB', 'ST', 'GQ');

-- Users Table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    country country_code NOT NULL,
    phone VARCHAR(20),
    plan plan_type DEFAULT 'BASICO',
    api_key VARCHAR(255) UNIQUE,
    is_active BOOLEAN DEFAULT true,
    is_verified BOOLEAN DEFAULT false,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_country ON users(country);

-- Free Fire Accounts Table
CREATE TABLE free_fire_accounts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    ff_account_id VARCHAR(100) NOT NULL,
    ff_username VARCHAR(100) NOT NULL,
    level INT DEFAULT 0,
    experience INT DEFAULT 0,
    created_at_ff TIMESTAMP,
    account_created_date DATE,
    last_synced TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, ff_account_id)
);

CREATE INDEX idx_ff_accounts_user_id ON free_fire_accounts(user_id);
CREATE INDEX idx_ff_accounts_ff_id ON free_fire_accounts(ff_account_id);

-- Likes Table
CREATE TABLE likes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sender_account_id UUID NOT NULL REFERENCES free_fire_accounts(id) ON DELETE CASCADE,
    receiver_ff_account_id VARCHAR(100) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    plan plan_type NOT NULL,
    status transaction_status DEFAULT 'PENDING',
    response_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_likes_sender ON likes(sender_account_id);
CREATE INDEX idx_likes_status ON likes(status);
CREATE INDEX idx_likes_created_at ON likes(created_at);

-- Guilds Table
CREATE TABLE guilds (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    guild_id VARCHAR(100) UNIQUE NOT NULL,
    guild_name VARCHAR(200) NOT NULL,
    level INT DEFAULT 1,
    total_members INT DEFAULT 0,
    leader_ff_id VARCHAR(100),
    country country_code,
    experience_points INT DEFAULT 0,
    total_likes_received INT DEFAULT 0,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_guilds_guild_id ON guilds(guild_id);
CREATE INDEX idx_guilds_country ON guilds(country);

-- Guild Members Table
CREATE TABLE guild_members (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    guild_id UUID NOT NULL REFERENCES guilds(id) ON DELETE CASCADE,
    ff_account_id VARCHAR(100) NOT NULL,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role VARCHAR(50) DEFAULT 'MEMBER',
    contribution_points INT DEFAULT 0
);

CREATE INDEX idx_guild_members_guild_id ON guild_members(guild_id);

-- Booyah Pass Table
CREATE TABLE booyah_passes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sender_account_id UUID NOT NULL REFERENCES free_fire_accounts(id) ON DELETE CASCADE,
    receiver_ff_account_id VARCHAR(100) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    status transaction_status DEFAULT 'PENDING',
    response_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_booyah_passes_sender ON booyah_passes(sender_account_id);
CREATE INDEX idx_booyah_passes_status ON booyah_passes(status);

-- Rewards Table
CREATE TABLE rewards (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    reward_type VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2),
    currency VARCHAR(3) DEFAULT 'USD',
    description TEXT,
    claimed BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    claimed_at TIMESTAMP
);

CREATE INDEX idx_rewards_user_id ON rewards(user_id);
CREATE INDEX idx_rewards_claimed ON rewards(claimed);

-- Transactions Table
CREATE TABLE transactions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    transaction_type VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2),
    currency VARCHAR(3) DEFAULT 'USD',
    status transaction_status DEFAULT 'PENDING',
    plan plan_type,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP
);

CREATE INDEX idx_transactions_user_id ON transactions(user_id);
CREATE INDEX idx_transactions_status ON transactions(status);
CREATE INDEX idx_transactions_created_at ON transactions(created_at);

-- API Usage Logs Table
CREATE TABLE api_usage_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    endpoint VARCHAR(255),
    method VARCHAR(10),
    status_code INT,
    response_time_ms INT,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_api_logs_user_id ON api_usage_logs(user_id);
CREATE INDEX idx_api_logs_created_at ON api_usage_logs(created_at);

-- Audit Log Table
CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    action VARCHAR(100),
    entity_type VARCHAR(50),
    entity_id VARCHAR(100),
    old_values JSONB,
    new_values JSONB,
    ip_address VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);

-- Plano de Preços
CREATE TABLE pricing_plans (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    plan_name plan_type UNIQUE NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    daily_limit INT,
    features JSONB,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default pricing plans
INSERT INTO pricing_plans (plan_name, price, daily_limit, features) VALUES
('BASICO', 3.00, 100, '{"likes_per_day": 100, "booyah_pass": false, "priority_support": false}'),
('PREMIUM', 4.19, 500, '{"likes_per_day": 500, "booyah_pass": true, "priority_support": false}'),
('ELITE', 5.09, -1, '{"likes_per_day": -1, "booyah_pass": true, "priority_support": true}');

-- Create Views
CREATE VIEW user_statistics AS
SELECT 
    u.id,
    u.email,
    u.country,
    u.plan,
    COUNT(DISTINCT fa.id) as total_accounts,
    COUNT(DISTINCT CASE WHEN l.status = 'SUCCESS' THEN l.id END) as successful_likes,
    COUNT(DISTINCT CASE WHEN bp.status = 'SUCCESS' THEN bp.id END) as successful_booyah_passes,
    COALESCE(SUM(CASE WHEN t.status = 'SUCCESS' THEN t.amount ELSE 0 END), 0) as total_spent
FROM users u
LEFT JOIN free_fire_accounts fa ON u.id = fa.user_id
LEFT JOIN likes l ON fa.id = l.sender_account_id
LEFT JOIN booyah_passes bp ON fa.id = bp.sender_account_id
LEFT JOIN transactions t ON u.id = t.user_id AND t.status = 'SUCCESS'
GROUP BY u.id, u.email, u.country, u.plan;
