package com.example.e_ticket_uz_railway.domain.dto.request;

import com.example.e_ticket_uz_railway.domain.enums.CarriageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarriagePostRequest {
    private CarriageType carriageType;
    private UUID railwayId;
}
