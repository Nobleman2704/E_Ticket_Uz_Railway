package com.example.e_ticket_uz_railway.dao;

import com.example.e_ticket_uz_railway.domain.entity.railwayFlight.RailwayFlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RailwayFlightDao extends JpaRepository<RailwayFlightEntity, UUID> {
    boolean existsRailwayFlightEntityByRailwayFlightName(String name);

}
