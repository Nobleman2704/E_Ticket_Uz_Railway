package com.example.e_ticket_uz_railway.service;

import com.example.e_ticket_uz_railway.dao.*;
import com.example.e_ticket_uz_railway.domain.dto.BaseResponse;
import com.example.e_ticket_uz_railway.domain.dto.request.SearchingPostRequest;
import com.example.e_ticket_uz_railway.domain.dto.request.TicketPostRequest;
import com.example.e_ticket_uz_railway.domain.dto.response.TicketGetResponse;
import com.example.e_ticket_uz_railway.domain.entity.card.CardEntity;
import com.example.e_ticket_uz_railway.domain.entity.seat.SeatEntity;
import com.example.e_ticket_uz_railway.domain.entity.ticket.TicketEntity;
import com.example.e_ticket_uz_railway.domain.entity.travel.TravelEntity;
import com.example.e_ticket_uz_railway.domain.entity.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TicketService implements BaseService<TicketEntity, BaseResponse<TicketGetResponse>>{
    private final TicketDao ticketDao;
    private final ModelMapper modelMapper;
    private final CardDao cardDao;
    private final SeatDao seatDao;
    private final TravelService travelService;
    private final TravelDao travelDao;

    public BaseResponse<List<TicketGetResponse>> findMyTicketsById(UUID userId){
        Optional<List<TicketEntity>> optionalTicketEntities = ticketDao.findTicketEntitiesByUsersId(userId);
        if (optionalTicketEntities.isEmpty()){
            return BaseResponse.<List<TicketGetResponse>>builder()
                    .status(404)
                    .message("You do not have any tickets")
                    .build();
        }

        List<TicketEntity> ticketEntities = optionalTicketEntities.get();

        return BaseResponse.<List<TicketGetResponse>>builder()
                .status(200)
                .message(ticketEntities + " result(s) found")
                .data(modelMapper.map(ticketEntities, new TypeToken<List<TicketEntity>>(){}.getType()))
                .build();
    }

    public BaseResponse<TicketGetResponse> bookSeat(TicketPostRequest ticketPostRequest){
        UUID cardId = ticketPostRequest.getCardId();
        Double travelPrice = ticketPostRequest.getTravelPrice();

        CardEntity cardEntity = cardDao.findById(cardId).get();
        if (cardEntity.getBalance()<travelPrice){
            return BaseResponse.<TicketGetResponse>builder()
                    .status(401)
                    .message("You do not have enough money " + cardEntity.getBalance())
                    .build();
        }


        UserEntity userEntity = cardEntity.getUsers();

        UUID seatId = ticketPostRequest.getSeatId();
        SeatEntity seatEntity = seatDao.findById(seatId).get();

        UUID railwayId = seatEntity.getCarriages().getRailways().getId();

        LinkedList<TravelEntity> travelEntities = travelDao
                .findTravelEntitiesByRailwaysIdOrderByCreated(railwayId).get();



        SearchingPostRequest searchingInfo = SearchingPostRequest.builder()
                .cityFrom(ticketPostRequest.getCityFrom())
                .cityTo(ticketPostRequest.getCityTo())
                .localDate(ticketPostRequest.getLocalDate())
                .build();
        Map<String, Object> travelInfo = travelService
                .existsRailwayFlightTravelBySearching(searchingInfo, travelEntities);

        cardEntity.setBalance(cardEntity.getBalance()-travelPrice);

        cardDao.save(cardEntity);

        TicketEntity ticket = TicketEntity.builder()
                .seats(seatEntity)
                .users(userEntity)
                .cityFrom(searchingInfo.getCityFrom())
                .cityTo(searchingInfo.getCityTo())
                .cityFromNumber((Integer) travelInfo.get("numberOfCityFrom"))
                .cityToNumber((Integer) travelInfo.get("numberOfCityTo"))
                .dateBegin((LocalDateTime) travelInfo.get("beginTime"))
                .dateEnd((LocalDateTime) travelInfo.get("endTime"))
                .travelPrice(ticketPostRequest.getTravelPrice())
                .travelDuration(String.valueOf(travelInfo.get("duration")))
                .build();

        ticketDao.save(ticket);

        return BaseResponse.<TicketGetResponse>builder()
                .status(200)
                .message("Successfully booked")
                .data(modelMapper.map(ticket, TicketGetResponse.class))
                .build();

    }



    @Override
    public BaseResponse<TicketGetResponse> create(TicketEntity ticketEntity) {
        return null;
    }

    @Override
    public BaseResponse<TicketGetResponse> getById(UUID id) {
        return null;
    }

    @Override
    public BaseResponse<TicketGetResponse> deleteById(UUID id) {
        return null;
    }
}
