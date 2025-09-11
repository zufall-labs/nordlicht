package shared

import (
    "sync"
)

// LatestADCValue holds the latest ADC value
var LatestADCValue map[string]interface{}

// ADCValueMutex protects access to LatestADCValue
var ADCValueMutex sync.Mutex