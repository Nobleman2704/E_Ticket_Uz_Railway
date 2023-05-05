package com.example.e_ticket_uz_railway.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RailwayFlightGetResponse {
    private String railwayFlightName;
    private LocalDateTime expirationDate;
}
