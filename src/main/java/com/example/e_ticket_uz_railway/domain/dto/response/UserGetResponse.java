package com.example.e_ticket_uz_railway.domain.dto.response;

import jakarta.persistence.Column;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserGetResponse {
    private UUID id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private boolean isDeleted;
    private String name;
    private String email;
    private String password;
}
