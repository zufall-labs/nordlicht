package mqtt

import (
    "fmt"
    "math/rand"
    "time"

    "ai.zufall.nordlicht.entropy/internal/shared"
    "github.com/eclipse/paho.mqtt.golang"
)

type ADCService struct {
    client mqtt.Client
}

func NewADCService(brokerURL string) *ADCService {
    opts := mqtt.NewClientOptions().AddBroker(brokerURL)
    opts.SetClientID("adc-mock-service")
    client := mqtt.NewClient(opts)
    if token := client.Connect(); token.Wait() && token.Error() != nil {
        panic(token.Error())
    }
    return &ADCService{client: client}
}

func (s *ADCService) Start() {
    go func() {
        for {
            raw := generateADCValue()
            voltage := calculateVoltage(raw)

            payload := fmt.Sprintf(`{"raw": %d, "voltage": %.4f}`, raw, voltage)

            token := s.client.Publish("adc/se", 1, false, payload)
            token.Wait()

            shared.ADCValueMutex.Lock()
            shared.LatestADCValue = map[string]interface{}{
                "raw":     raw,
                "voltage": voltage,
            }
            shared.ADCValueMutex.Unlock()

            time.Sleep(100 * time.Millisecond)
        }
    }()

    token := s.client.Subscribe("adc/se", 1, func(client mqtt.Client, msg mqtt.Message) {
        fmt.Printf("Published: %s\n", msg.Payload())
    })
    token.Wait()
}

func generateADCValue() int {
    maxValue := 0x7FFFFFFF
    return rand.Intn(maxValue + 1)
}

func calculateVoltage(raw int) float64 {
    const maxRaw = 0x7FFFFFFF
    const refVoltage = 5.08
    return float64(raw) / float64(maxRaw) * refVoltage
}
