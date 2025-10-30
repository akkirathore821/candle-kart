package com.candlekart.auth_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {
    private String token;
}
