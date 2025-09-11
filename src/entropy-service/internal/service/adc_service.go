package service

import (
    "bytes"
    "encoding/json"
    "fmt"
    "net/http"
    "ai.zufall.nordlicht.entropy/internal/shared"
)

func GetADCValueWithCalculation() (map[string]interface{}, error) {
    shared.ADCValueMutex.Lock()
    defer shared.ADCValueMutex.Unlock()

    if shared.LatestADCValue == nil {
        return nil, fmt.Errorf("no data available")
    }

    payload, err := json.Marshal(shared.LatestADCValue)
    if err != nil {
        return nil, err
    }

    resp, err := http.Post("http://calculation-service:8080/calculate", "application/json", bytes.NewBuffer(payload))
    if err != nil {
        return nil, err
    }
    defer resp.Body.Close()

    var result map[string]interface{}
    if err := json.NewDecoder(resp.Body).Decode(&result); err != nil {
        return nil, err
    }
	
    return result, nil
}