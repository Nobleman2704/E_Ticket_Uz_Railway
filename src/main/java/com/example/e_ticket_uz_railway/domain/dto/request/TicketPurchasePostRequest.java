package com.example.e_ticket_uz_railway.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TicketPurchasePostRequest {
    private SearchingPostRequest searchingInfo;
    private UUID cardId;
    private UUID seatId;
    private Double travelPrice;
}