package com.example.e_ticket_uz_railway.dao;

import com.example.e_ticket_uz_railway.domain.entity.travel.TravelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TravelDao extends JpaRepository<TravelEntity, UUID> {
}