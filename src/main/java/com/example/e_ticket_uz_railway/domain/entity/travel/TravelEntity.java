package com.example.e_ticket_uz_railway.domain.entity.travel;

import com.example.e_ticket_uz_railway.domain.entity.BaseEntity;
import com.example.e_ticket_uz_railway.domain.entity.railwayFlight.RailwayFlightEntity;
import com.example.e_ticket_uz_railway.domain.enums.CityName;
import jakarta.persistence.Column;
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
    @Column(name = "city_from")
    private CityName cityFrom;
    @Column(name = "city_from_number")
    private int cityFromNumber;
    @Column(name = "city_to")
    private CityName cityTo;
    @Column(name = "city_to_number")
    private int cityToNumber;
    @Column(name = "plascard_price")
    private Double plascardPrice;
    @Column(name = "kupe_price")
    private Double kupePrice;
    @Column(name = "vip_price")
    private Double vipPrice;
    @Column(name = "date_begin")
    private LocalDateTime dateBegin;
    @Column(name = "date_end")
    private LocalDateTime dateEnd;
    @ManyToOne
    @JoinColumn(name = "railway_id")
    private RailwayFlightEntity railways;
}
