package com.example.e_ticket_uz_railway.domain.dto.response;

import com.example.e_ticket_uz_railway.domain.entity.railwayFlight.RailwayFlightEntity;
import com.example.e_ticket_uz_railway.domain.entity.seat.SeatEntity;
import com.example.e_ticket_uz_railway.domain.enums.CarriageType;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CarriageGetResponse {
    private UUID id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private boolean isDeleted;
    private CarriageType carriageType;
    private RailwayFlightEntity railways;
    private List<SeatEntity> seats;
}
