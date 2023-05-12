package com.example.e_ticket_uz_railway.domain.dto.request;


import com.example.e_ticket_uz_railway.domain.enums.CityName;
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
    private CityName cityFrom;
    private CityName cityTo;
    private Double vipPrice;
    private Double kupePrice;
    private Double plascardPrice;
    private LocalDateTime dateBegin;
    private LocalDateTime dateEnd;
    private UUID railwayId;
}
