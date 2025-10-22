package handlers

import (
    "errors"

    "github.com/gofiber/fiber/v2"
)

// ErrorResponse sends a standardized error response
func ErrorResponse(c *fiber.Ctx, status int, message string) error {
    return c.Status(status).JSON(fiber.Map{
        "error": message,
    })
}

// ErrorHandler is the custom error handler for the application
func ErrorHandler(c *fiber.Ctx, err error) error {
    code := fiber.StatusInternalServerError
    message := "Internal Server Error"

    var e *fiber.Error
    if errors.As(err, &e) {
        code = e.Code
        message = e.Message
    }

    return ErrorResponse(c, code, message)
}
