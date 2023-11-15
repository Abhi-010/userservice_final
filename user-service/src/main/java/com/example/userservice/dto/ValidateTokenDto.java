package com.example.userservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenDto {
    private Long userid;
    private String token;
}
