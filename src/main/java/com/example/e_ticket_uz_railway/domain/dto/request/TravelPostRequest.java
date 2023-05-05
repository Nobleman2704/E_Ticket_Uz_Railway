package com.example.e_ticket_uz_railway.domain.dto.request;


import com.example.e_ticket_uz_railway.domain.enums.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TravelPostRequest {
    private City cityFrom;
    private City cityTo;
    private Double travelPrice;
    private LocalDateTime dateBegin;
    private LocalDateTime dateEnd;
    private UUID railwayId;
}
