-- Create databases for each microservice
CREATE DATABASE safepilgrim_digitalid;
CREATE DATABASE safepilgrim_emergency;
CREATE DATABASE safepilgrim_geofencing;
CREATE DATABASE safepilgrim_analytics;

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE safepilgrim_digitalid TO safepilgrim;
GRANT ALL PRIVILEGES ON DATABASE safepilgrim_emergency TO safepilgrim;
GRANT ALL PRIVILEGES ON DATABASE safepilgrim_geofencing TO safepilgrim;
GRANT ALL PRIVILEGES ON DATABASE safepilgrim_analytics TO safepilgrim;

-- Connect to each database and create basic tables
\c safepilgrim_digitalid;

CREATE TABLE digital_ids (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(255) NOT NULL,
    passport_number VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    nationality VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    expiry_date DATE NOT NULL,
    blockchain_hash VARCHAR(255),
    qr_code TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE emergency_contacts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    digital_id UUID REFERENCES digital_ids(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    relationship VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(255)
);

\c safepilgrim_emergency;

CREATE TABLE emergency_alerts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(255) NOT NULL,
    alert_type VARCHAR(50) NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    accuracy FLOAT,
    audio_data TEXT,
    user_message TEXT,
    status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE emergency_responses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    alert_id UUID REFERENCES emergency_alerts(id) ON DELETE CASCADE,
    responder_name VARCHAR(255),
    responder_agency VARCHAR(255),
    contact_number VARCHAR(20),
    estimated_arrival INTEGER,
    status VARCHAR(50) DEFAULT 'DISPATCHED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

\c safepilgrim_geofencing;

CREATE TABLE geofences (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    center_latitude DECIMAL(10, 8) NOT NULL,
    center_longitude DECIMAL(11, 8) NOT NULL,
    radius FLOAT NOT NULL,
    enter_alert BOOLEAN DEFAULT true,
    exit_alert BOOLEAN DEFAULT true,
    dwell_alert BOOLEAN DEFAULT false,
    notification_message TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE geofence_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    geofence_id UUID REFERENCES geofences(id) ON DELETE CASCADE,
    user_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

\c safepilgrim_analytics;

CREATE TABLE safety_scores (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(255) NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    safety_score INTEGER NOT NULL,
    risk_level VARCHAR(20) NOT NULL,
    factors JSONB,
    recommendations TEXT[],
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE risk_predictions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(255) NOT NULL,
    predicted_risk VARCHAR(20) NOT NULL,
    confidence FLOAT NOT NULL,
    risk_factors JSONB,
    alternative_routes JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE anomalies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(255) NOT NULL,
    anomaly_type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    description TEXT,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    detected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
