package app

import (
    "github.com/gofiber/fiber/v2"
    "ai.zufall.nordlicht.entropy/internal/service"
)

type App struct {
    *fiber.App
}

func New(cfg interface{}) *App {
    app := fiber.New()

    app.Get("/entropy", func(c *fiber.Ctx) error {
        result, err := service.GetADCValueWithCalculation()
        if err != nil {
            return c.Status(500).JSON(fiber.Map{"error": err.Error()})
        }

        return c.JSON(result)
    })

    return &App{app}
}