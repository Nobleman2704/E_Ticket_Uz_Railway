package com.example.e_ticket_uz_railway.domain.dto.response;

import com.example.e_ticket_uz_railway.domain.entity.seat.SeatEntity;
import com.example.e_ticket_uz_railway.domain.enums.CityName;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TicketGetResponse {
    private SeatEntity seats;
    private CityName cityFrom;
    private CityName cityTo;
    private Double travelPrice;
    private String travelDuration;
    private LocalDateTime dateBegin;
    private LocalDateTime dateEnd;
}
