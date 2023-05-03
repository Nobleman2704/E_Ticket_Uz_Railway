package com.example.e_ticket_uz_railway.domain.entity.ticket;

import com.example.e_ticket_uz_railway.domain.entity.seat.SeatEntity;
import com.example.e_ticket_uz_railway.domain.entity.BaseEntity;
import com.example.e_ticket_uz_railway.domain.entity.user.UserEntity;
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
@Entity(name = "tickets")
public class TicketEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity users;

    @ManyToOne
    @JoinColumn(name = "seats_id")
    private SeatEntity seats;
    private City cityFrom;
    private City cityTo;
    private LocalDateTime dateBegin;
    //for dateBegin : currentDate merge travel.begin.time

    private LocalDateTime dateEnd;
    //for dateEnd : currentDatetime + (travel.end.datetime - travel.begin.datetime)
}
