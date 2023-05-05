package com.example.e_ticket_uz_railway.domain.entity.seat;

import com.example.e_ticket_uz_railway.domain.entity.BaseEntity;
import com.example.e_ticket_uz_railway.domain.entity.ticket.TicketEntity;
import com.example.e_ticket_uz_railway.domain.entity.carriage.TrainCarriageEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "seats")
public class SeatEntity extends BaseEntity {
    @Column(name = "seat_number")
    private int seatNumber;

    @OneToMany(mappedBy = "seats")
    private List<TicketEntity> tickets;

    @ManyToOne
    @JoinColumn(name = "carriage_id")
    private TrainCarriageEntity carriages;
}
