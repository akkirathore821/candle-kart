package com.candlekart.order_service.exc;

public class ValidationException extends RuntimeException{
    public ValidationException(String message) {
        super(message);
    }
}
