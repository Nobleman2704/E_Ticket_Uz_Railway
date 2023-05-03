package com.example.e_ticket_uz_railway.domain.entity.station;

import com.example.e_ticket_uz_railway.domain.entity.railway.RailwayEntity;
import com.example.e_ticket_uz_railway.domain.entity.BaseEntity;
import com.example.e_ticket_uz_railway.domain.enums.City;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "travels")
public class TravelEntity extends BaseEntity {
    private City cityFrom;
    private City cityTo;
    private Double travelPrice;
    private LocalDateTime dateBegin;
    private LocalDateTime dateEnd;
    @ManyToOne
    @JoinColumn(name = "railway_id")
    private RailwayEntity railways;
}
