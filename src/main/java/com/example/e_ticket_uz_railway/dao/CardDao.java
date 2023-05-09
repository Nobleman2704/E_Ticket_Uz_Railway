package com.example.e_ticket_uz_railway.dao;

import com.example.e_ticket_uz_railway.domain.entity.card.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardDao extends JpaRepository<CardEntity, UUID> {
    Optional<List<CardEntity>> findCardEntitiesByUsersId(UUID id);
}
