package com.example.e_ticket_uz_railway.domain.entity.carriage;

import com.example.e_ticket_uz_railway.domain.entity.railwayFlight.RailwayFlightEntity;
import com.example.e_ticket_uz_railway.domain.entity.seat.SeatEntity;
import com.example.e_ticket_uz_railway.domain.entity.BaseEntity;
import com.example.e_ticket_uz_railway.domain.enums.CarriageType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "carriages")
public class TrainCarriageEntity extends BaseEntity {

    @Column(name = "carriage_number")
    private int carriageNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "carriage_type")
    private CarriageType carriageType;

    @ManyToOne
    @JoinColumn(name = "railway_id")
    private RailwayFlightEntity railways;

    @OneToMany(mappedBy = "carriages", cascade = CascadeType.ALL)
    private List<SeatEntity> seats;
}
