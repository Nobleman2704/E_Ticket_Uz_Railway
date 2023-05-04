package com.example.e_ticket_uz_railway.domain.entity.carriage;

import com.example.e_ticket_uz_railway.domain.entity.seat.SeatEntity;
import com.example.e_ticket_uz_railway.domain.entity.BaseEntity;
import com.example.e_ticket_uz_railway.domain.entity.railway.RailwayEntity;
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

    @Column(name = "carriage_type")
    private CarriageType carriageType;

    @ManyToOne
    @JoinColumn(name = "railway_id")
    private RailwayEntity railways;

    @OneToMany(mappedBy = "carriages")
    private List<SeatEntity> seats;
}
