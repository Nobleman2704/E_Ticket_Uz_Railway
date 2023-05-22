package com.example.e_ticket_uz_railway.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse <T>{
    private int status;
    private String message;
    private T data;
}
