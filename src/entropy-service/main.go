package main

import (
    "log"
    "os"

    "ai.zufall.nordlicht.entropy/internal/app"
    "ai.zufall.nordlicht.entropy/internal/config"
    "ai.zufall.nordlicht.entropy/internal/mqtt"
)

func main() {
    cfg, err := config.Load()
    if err != nil {
        log.Fatalf("Failed to load configuration: %v", err)
    }

    aiApp := app.New(cfg)

    mqttService := mqtt.NewADCService("tcp://localhost:1883")

    mqttService.Start()

    port := os.Getenv("PORT")
    if port == "" {
        port = "3000"
    }

    log.Printf("Server starting on port %s", port)
    if err := aiApp.Listen(":" + port); err != nil {
        log.Fatalf("Failed to start server: %v", err)
    }
}