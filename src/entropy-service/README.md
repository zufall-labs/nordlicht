# 🚀 Entropy ADC MQTT Mock Service

This project is a **Go-based mock ADC (Analog-to-Digital Converter)** service that simulates a 32-bit ADC in
**Single-Ended (SE) mode**, publishing values via **MQTT** to a topic (`adc/se`).
It also includes a **Fiber HTTP API** with an endpoint to retrieve the latest ADC value on demand.

---

## 📦 Features

- ✅ Simulates a 32-bit ADC in SE mode (0V–5V).
- ✅ Publishes ADC values every 100ms via MQTT.
- ✅ Stores the **latest ADC value** in memory.
- ✅ Provides an **HTTP endpoint** to retrieve the latest ADC value (`/adc/get`).
- ✅ Fully modular and testable.
- ✅ Uses Docker for MQTT broker (Mosquitto).

---

## 📁 Project Structure

```
entropy-service/
├── main.go
├── go.mod
├── internal/
│   ├── app/
│   │   └── app.go
│   ├── config/
│   │   └── config.go
│   ├── mqtt/
│   │   └── adc.go
│   └── shared/
│       └── adc.go
└── mosquitto.conf
```

---

## 🛠️ Requirements

- Go 1.20 or higher
- Docker (for Mosquitto MQTT broker)

---

## 🚀 Setup & Run

### 1. Start Mosquitto MQTT Broker

```bash
docker run -it --rm \
  -p 1883:1883 \
  -v ${PWD}/mosquitto.conf:/mosquitto/config/mosquitto.conf \
  eclipse-mosquitto
```

> Ensure `mosquitto.conf` exists in the project root with:

```conf
listener 1883
allow_anonymous true
```

---

### 2. Run the Go App

```bash
go run main.go
```

This will:

- Start the MQTT ADC mock service.
- Begin publishing ADC values to `adc/se`.
- Start the Fiber HTTP server on port `3000`.

---

## 🌐 API Endpoint

### ✅ Get the Latest ADC Value

**Endpoint:** `GET /adc/get`

**Response Example:**

```json
{
    "raw": 1215619851,
    "voltage": 2.8756
}
```

---

## 🧪 Testing with `curl`

```bash
curl http://localhost:3000/adc/get
```

---
