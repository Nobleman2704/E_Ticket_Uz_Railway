package com.example.e_ticket_uz_railway.domain.entity.card;

import com.example.e_ticket_uz_railway.domain.entity.BaseEntity;
import com.example.e_ticket_uz_railway.domain.entity.user.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "cards")
public class CardEntity extends BaseEntity {
    private String number;
    private double balance;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity users;
}
