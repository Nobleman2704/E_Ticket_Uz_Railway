package com.example.e_ticket_uz_railway.domain.entity.railway;

import com.example.e_ticket_uz_railway.domain.entity.carriage.TrainCarriageEntity;
import com.example.e_ticket_uz_railway.domain.entity.BaseEntity;
import com.example.e_ticket_uz_railway.domain.entity.station.TravelEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.LinkedList;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "railways")
public class RailwayEntity extends BaseEntity {

    @OneToMany(mappedBy = "railways")
    private LinkedList<TravelEntity> travels;

    @OneToMany(mappedBy = "railways")
    private LinkedList<TrainCarriageEntity> trainCarriages;
}
