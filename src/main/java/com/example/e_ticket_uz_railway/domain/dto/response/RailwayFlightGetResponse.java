package com.example.e_ticket_uz_railway.domain.dto.response;

import com.example.e_ticket_uz_railway.domain.entity.carriage.TrainCarriageEntity;
import com.example.e_ticket_uz_railway.domain.entity.travel.TravelEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RailwayFlightGetResponse {
    private UUID id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private boolean isDeleted;
    private String railwayFlightName;
    private LocalDateTime expirationDate;
    private LinkedList<TrainCarriageEntity> trainCarriages;
    private LinkedList<TravelEntity> travels;
}
