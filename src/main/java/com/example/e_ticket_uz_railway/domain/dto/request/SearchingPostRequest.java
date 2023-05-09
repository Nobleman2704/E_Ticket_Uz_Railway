package com.example.e_ticket_uz_railway.domain.dto.request;

import com.example.e_ticket_uz_railway.domain.enums.CityName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchingPostRequest {
    private CityName cityFrom;
    private CityName cityTo;
    private LocalDate localDate;
}
