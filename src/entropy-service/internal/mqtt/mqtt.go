package mqtt

import (
    "context"
)

type MQTTService interface {
    Start(ctx context.Context)
}