package com.example.e_ticket_uz_railway.domain.dto.response;

import com.example.e_ticket_uz_railway.domain.dto.request.SearchingPostRequest;
import com.example.e_ticket_uz_railway.domain.entity.railwayFlight.RailwayFlightEntity;
import com.example.e_ticket_uz_railway.domain.entity.seat.SeatEntity;
import com.example.e_ticket_uz_railway.domain.enums.CarriageType;
import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CarriageSearchGetResponse {
    private SearchingPostRequest searchingInfo;
    private int carriageNumber;
    private CarriageType carriageType;
    private List<SeatEntity> seats;
    private Double travelPrice;
    private UUID railwayId;
}
