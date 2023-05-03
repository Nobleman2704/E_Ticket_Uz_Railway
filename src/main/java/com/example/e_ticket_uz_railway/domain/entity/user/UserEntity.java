package com.example.e_ticket_uz_railway.domain.entity.user;

import com.example.e_ticket_uz_railway.domain.entity.BaseEntity;
import com.example.e_ticket_uz_railway.domain.entity.card.CardEntity;
import com.example.e_ticket_uz_railway.domain.entity.ticket.TicketEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "users")
public class UserEntity extends BaseEntity {
    private String name;

    private String email;

    private String password;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @OneToMany(mappedBy = "users")
    private List<CardEntity> cards;

    @OneToMany(mappedBy = "users")
    private List<TicketEntity> tickets;
}
