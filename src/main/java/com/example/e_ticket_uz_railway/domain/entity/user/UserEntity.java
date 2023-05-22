package com.example.e_ticket_uz_railway.domain.entity.user;

import com.example.e_ticket_uz_railway.domain.entity.BaseEntity;
import com.example.e_ticket_uz_railway.domain.entity.card.CardEntity;
import com.example.e_ticket_uz_railway.domain.entity.ticket.TicketEntity;
import com.example.e_ticket_uz_railway.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "users")
public class UserEntity extends BaseEntity implements UserDetails {
    private String name;

    @Override
    public String toString() {
        return "UserEntity{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin=" + isAdmin +
                ", userRole=" + userRole +
                ", id=" + id +
                ", created=" + created +
                ", updated=" + updated +
                ", isDeleted=" + isDeleted +
                '}';
    }

    @Column(unique = true)
    private String email;
    private String password;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "users")
    private List<CardEntity> cards;

    @OneToMany(mappedBy = "users")
    private List<TicketEntity> tickets;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}