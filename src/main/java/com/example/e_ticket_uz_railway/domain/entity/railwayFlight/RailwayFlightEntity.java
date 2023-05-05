package com.example.e_ticket_uz_railway.domain.entity.railwayFlight;

import com.example.e_ticket_uz_railway.domain.entity.carriage.TrainCarriageEntity;
import com.example.e_ticket_uz_railway.domain.entity.BaseEntity;
import com.example.e_ticket_uz_railway.domain.entity.travel.TravelEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "railways")
public class RailwayFlightEntity extends BaseEntity {
    @Column(name = "railway_flight_name")
    private String railwayFlightName;
    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @OneToMany(mappedBy = "railways")
    private LinkedList<TravelEntity> travels;

    @OneToMany(mappedBy = "railways")
    private LinkedList<TrainCarriageEntity> trainCarriages;
}
