package com.example.e_ticket_uz_railway.service;

import com.example.e_ticket_uz_railway.dao.*;
import com.example.e_ticket_uz_railway.domain.dto.BaseResponse;
import com.example.e_ticket_uz_railway.domain.dto.request.SearchingPostRequest;
import com.example.e_ticket_uz_railway.domain.dto.request.TravelPostRequest;
import com.example.e_ticket_uz_railway.domain.dto.response.TravelGetResponse;
import com.example.e_ticket_uz_railway.domain.entity.carriage.TrainCarriageEntity;
import com.example.e_ticket_uz_railway.domain.entity.railwayFlight.RailwayFlightEntity;
import com.example.e_ticket_uz_railway.domain.entity.seat.SeatEntity;
import com.example.e_ticket_uz_railway.domain.entity.ticket.TicketEntity;
import com.example.e_ticket_uz_railway.domain.entity.travel.TravelEntity;
import com.example.e_ticket_uz_railway.domain.enums.CarriageType;
import com.example.e_ticket_uz_railway.domain.enums.CityName;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TravelService implements BaseService<TravelPostRequest, BaseResponse<TravelGetResponse>> {
    private final ModelMapper modelMapper;
    private final RailwayFlightDao railwayFlightDao;
    private final TravelDao travelDao;
    private final TrainCarriageDao carriageDao;
    private final SeatDao seatDao;
    private final TicketDao ticketDao;

    @Override
    public BaseResponse<TravelGetResponse> create(TravelPostRequest travelPostRequest) {
        TravelEntity travelEntity = modelMapper.map(travelPostRequest, TravelEntity.class);

        RailwayFlightEntity railwayFlightEntity = railwayFlightDao.findById(travelPostRequest.getRailwayId()).get();
        travelEntity.setRailways(railwayFlightEntity);
        LinkedList<TravelEntity> travels = travelDao
                .findTravelEntitiesByRailwaysIdOrderByCreated(travelPostRequest.getRailwayId()).get();


        if (travels.isEmpty()) {
            travelEntity.setCityFromNumber(1);
            travelEntity.setCityToNumber(2);
        } else {
            TravelEntity lastTravel = travels.getLast();
            travelEntity.setCityFrom(lastTravel.getCityTo());
            travelEntity.setDateBegin(lastTravel.getDateEnd());
            travelEntity.setCityFromNumber(lastTravel.getCityToNumber());
            travelEntity.setCityToNumber(lastTravel.getCityToNumber() + 1);
        }

        boolean checkPrice = isTravelPriceNotValid(travelEntity);
        if (checkPrice) {
            return BaseResponse.<TravelGetResponse>builder()
                    .status(404)
                    .message("There is an error with travel price types")
                    .build();
        }

        boolean check = isTravelValid(travelEntity);

        if (check) {
            travelDao.save(travelEntity);

            return BaseResponse.<TravelGetResponse>builder()
                    .status(200)
                    .message("Successfully created")
                    .data(modelMapper.map(travelEntity, TravelGetResponse.class))
                    .build();
        }

        return BaseResponse.<TravelGetResponse>builder()
                .status(200)
                .message("Created travel info is not valid")
                .data(modelMapper.map(travelEntity, TravelGetResponse.class))
                .build();
    }

    private boolean isTravelPriceNotValid(TravelEntity travelEntity) {
        Double kupePrice = travelEntity.getKupePrice();
        Double plascardPrice = travelEntity.getPlascardPrice();
        Double vipPrice = travelEntity.getVipPrice();

        return vipPrice <= 0 || plascardPrice <= 0 || kupePrice <= 0 ||
                plascardPrice >= kupePrice || kupePrice >= vipPrice;
    }

    private boolean isTravelValid(TravelEntity travelEntity) {
        CityName cityTo = travelEntity.getCityTo();
        CityName cityFrom = travelEntity.getCityFrom();
        LocalDateTime dateEnd = travelEntity.getDateEnd();
        LocalDateTime dateBegin = travelEntity.getDateBegin();

        if (dateBegin.isBefore(LocalDateTime.now())){
            return false;
        }

        if (Objects.equals(cityFrom, cityTo)){
            return false;
        }

        if (dateBegin.isAfter(dateEnd) || Objects.equals(dateEnd, dateBegin)) {
            return false;
        }

        RailwayFlightEntity railways = travelEntity.getRailways();

        if (railways.getExpirationDate().isBefore(ChronoLocalDate.from(dateBegin))) {
            return false;
        }

        LinkedList<TravelEntity> travels = travelDao
                .findTravelEntitiesByRailwaysIdOrderByCreated(railways.getId()).get();

        for (TravelEntity travel : travels) {
            if (Objects.equals(travel.getCityFrom(), cityTo)
                    || Objects.equals(travel.getCityTo(), cityTo)) {
                return false;
            }
        }
        return true;
    }

    public BaseResponse<List<TravelGetResponse>> searchingTravels(SearchingPostRequest searchingPostRequest) {
        CityName cityFrom = searchingPostRequest.getCityFrom();
        CityName cityTo = searchingPostRequest.getCityTo();
        LocalDate localDate = searchingPostRequest.getLocalDate();

        if (Objects.equals(cityFrom, cityTo)) {
            return BaseResponse.<List<TravelGetResponse>>builder()
                    .status(400)
                    .message("Please choose different city names")
                    .build();
        }
        if (localDate.isBefore(LocalDate.now())) {
            return BaseResponse.<List<TravelGetResponse>>builder()
                    .status(400)
                    .message("Please choose current or later dates")
                    .build();
        }

        List<RailwayFlightEntity> railwayFlightEntities = railwayFlightDao.findAll();

        List<TravelGetResponse> travelGetResponses = new LinkedList<>();

        for (RailwayFlightEntity railwayFlightEntity : railwayFlightEntities) {
            Optional<TravelGetResponse> optionalTravel = findTravel(
                    searchingPostRequest,
                    railwayFlightEntity.getId());
            optionalTravel.ifPresent(travelGetResponses::add);
        }

        if (travelGetResponses.isEmpty()) {
            return BaseResponse.<List<TravelGetResponse>>builder()
                    .status(404)
                    .message("No travels found by this info: "
                            + cityFrom + ", "
                            + cityTo + ", "
                            + localDate)
                    .build();
        }

        return BaseResponse.<List<TravelGetResponse>>builder()
                .status(200)
                .message(travelGetResponses.size() + " result(s) found")
                .data(travelGetResponses)
                .build();
    }

    private Optional<TravelGetResponse> findTravel(SearchingPostRequest searchingPostRequest, UUID railwayId) {

        LocalDate localDate = searchingPostRequest.getLocalDate();

        Optional<LinkedList<TravelEntity>> optionalTravelEntities = travelDao
                .findTravelEntitiesByRailwaysIdOrderByCreated(railwayId);

        LocalDate expirationDate = railwayFlightDao.findById(railwayId).get().getExpirationDate();

        if (localDate.isAfter(expirationDate)){
            return Optional.empty();
        }

        if (optionalTravelEntities.isEmpty()){
            return Optional.empty();
        }

        Optional<List<TrainCarriageEntity>> optionalCarriages = carriageDao
                .findTrainCarriageEntitiesByRailwaysIdOrderByCreated(railwayId);

        Map<String, Object> travelInfo = existsRailwayFlightTravelBySearching(searchingPostRequest,
                optionalTravelEntities.get());

        if (travelInfo.isEmpty() || optionalCarriages.isEmpty()) {
            return Optional.empty();
        }

        List<TrainCarriageEntity> carriageEntityList = carriageDao
                .findTrainCarriageEntitiesByRailwaysIdOrderByCreated(railwayId).get();

        int plascardSeatCount = 0;
        int kupeSeatCount = 0;
        int vipSeatCount = 0;

        for (TrainCarriageEntity carriageEntity : carriageEntityList) {
            CarriageType carriageType = carriageEntity.getCarriageType();
            int seatCount = findAvailableSeats(travelInfo, carriageEntity.getId()).size();
            switch (carriageType) {
                case PLASCARD -> plascardSeatCount += seatCount;
                case KUPE -> kupeSeatCount += seatCount;
                default -> vipSeatCount += seatCount;
            }
        }
        if (plascardSeatCount == 0 && kupeSeatCount == 0 && vipSeatCount == 0) {
            return Optional.empty();
        }

        return Optional.of(TravelGetResponse.builder()
                .cityFrom(searchingPostRequest.getCityFrom())
                .cityTo(searchingPostRequest.getCityTo())
                .dateBegin((LocalDateTime) travelInfo.get("beginTime"))
                .dateEnd((LocalDateTime) travelInfo.get("endTime"))
                .timeDuration((String) travelInfo.get("duration"))
                .railways(railwayFlightDao.findById(railwayId).get())
                .plascardPrice((Double) travelInfo.get("plascardPrice"))
                .kupePrice((Double) travelInfo.get("kupePrice"))
                .vipPrice((Double) travelInfo.get("vipPrice"))
                .plascardSeatAmount(plascardSeatCount)
                .kupeSeatAmount(kupeSeatCount)
                .vipSeatAmount(vipSeatCount)
                .build());
    }

    public List<SeatEntity> findAvailableSeats(Map<String, Object> travelInfo,
                                   UUID carriageId) {
        List<SeatEntity> seatEntities = seatDao.findSeatEntitiesByCarriagesId(carriageId).get();

        List<SeatEntity> availableSeats = new LinkedList<>();

        for (SeatEntity seatEntity : seatEntities) {
            if (isSeatNotBooked(travelInfo, seatEntity.getId())){
                availableSeats.add(seatEntity);
            }
        }

        return availableSeats;
    }

    private boolean isSeatNotBooked(Map<String, Object> travelInfo,
                                    UUID seatId) {

        Optional<List<TicketEntity>> optionalTicketEntities = ticketDao.findTicketEntitiesBySeatsId(seatId);
        if (optionalTicketEntities.isEmpty()) {
            return true;
        }

        List<TicketEntity> ticketEntities = optionalTicketEntities.get();
        for (TicketEntity ticketEntity : ticketEntities) {
            if (isCrossed(travelInfo, ticketEntity)) {
                return false;
            }
        }
        return true;
    }

    private boolean isCrossed(Map<String, Object> travelInfo,
                              TicketEntity ticketEntity) {

        int sCityFromNumber = (int) travelInfo.get("numberOfCityFrom");
        int sCityToNumber = (int) travelInfo.get("numberOfCityTo");
        LocalDateTime sBeginDateTime = (LocalDateTime) travelInfo.get("beginTime");
        LocalDate sBeginTime = LocalDate.from(sBeginDateTime);

        int tCityFromNumber = ticketEntity.getCityFromNumber();
        int tCityToNumber = ticketEntity.getCityToNumber();
        LocalDate tBeginTime = LocalDate.from(ticketEntity.getDateBegin());

        if ((sCityToNumber <= tCityFromNumber ||
                sCityFromNumber >= tCityToNumber)){
            return false;
        }

        return sBeginTime.equals(tBeginTime);
    }

    public Map<String, Object> existsRailwayFlightTravelBySearching(SearchingPostRequest searchingPostRequest,
                                                                     List<TravelEntity> travelEntities) {
        CityName cityFrom = searchingPostRequest.getCityFrom();
        CityName cityTo = searchingPostRequest.getCityTo();
        LocalDate searchingDate = searchingPostRequest.getLocalDate();


        boolean cityFromExists = false;

        Map<String, Object> travelInfo = new HashMap<>();
        double plascardPrice = 0;
        double kupePrice = 0;
        double vipPrice = 0;

        LocalDateTime tBeginTime = null;

        for (TravelEntity travelEntity : travelEntities) {
            if (!cityFromExists && Objects.equals(travelEntity.getCityFrom(), cityFrom)) {
                tBeginTime = travelEntity.getDateBegin();
                travelInfo.put("numberOfCityFrom", travelEntity.getCityFromNumber());
                cityFromExists = true;
            }
            if (cityFromExists) {
                plascardPrice += travelEntity.getPlascardPrice();
                kupePrice += travelEntity.getKupePrice();
                vipPrice += travelEntity.getVipPrice();
            }
            if (cityFromExists && Objects.equals(travelEntity.getCityTo(), cityTo)) {
                LocalDateTime beginTime = LocalDateTime.of(searchingDate, LocalTime.from(tBeginTime));
                travelInfo.put("beginTime", beginTime);

                LocalDateTime tEndTime = travelEntity.getDateEnd();
                Duration duration = Duration.between(tBeginTime, tEndTime);

                String timeDuration = ((duration.toHours()/24!=0)?(duration.toHours()/24+" day "):"") + "" + (int)(duration.toHours()%24) + ":"+(int)(duration.toMinutes()%60);

                LocalDateTime endTime = beginTime.plus(duration);

                travelInfo.put("duration", timeDuration);
                travelInfo.put("endTime", endTime);
                travelInfo.put("plascardPrice", plascardPrice);
                travelInfo.put("kupePrice", kupePrice);
                travelInfo.put("vipPrice", vipPrice);
                travelInfo.put("numberOfCityTo", travelEntity.getCityToNumber());
                return travelInfo;
            }
        }
        return Collections.emptyMap();
    }


    @Override
    public BaseResponse<TravelGetResponse> getById(UUID id) {
        return null;
    }

    @Override
    public BaseResponse<TravelGetResponse> deleteById(UUID id) {
        return null;
    }

    public List<TravelGetResponse> findTravelsByRailwayFlightId(UUID railwayFlightId) {
        Optional<LinkedList<TravelEntity>> optionalTravelEntities = travelDao
                .findTravelEntitiesByRailwaysIdOrderByCreated(railwayFlightId);

        if (optionalTravelEntities.isEmpty()){
            return Collections.emptyList();
        }
        return modelMapper.map(optionalTravelEntities.get(), new TypeToken<LinkedList<TravelGetResponse>>(){}
                .getType());
    }
}