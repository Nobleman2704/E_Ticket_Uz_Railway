package com.example.e_ticket_uz_railway.service;

import com.example.e_ticket_uz_railway.dao.RailwayFlightDao;
import com.example.e_ticket_uz_railway.dao.TrainCarriageDao;
import com.example.e_ticket_uz_railway.dao.TravelDao;
import com.example.e_ticket_uz_railway.domain.dto.BaseResponse;
import com.example.e_ticket_uz_railway.domain.dto.request.CarriagePostRequest;
import com.example.e_ticket_uz_railway.domain.dto.request.SearchingPostRequest;
import com.example.e_ticket_uz_railway.domain.dto.response.CarriageGetResponse;
import com.example.e_ticket_uz_railway.domain.dto.response.CarriageSearchGetResponse;
import com.example.e_ticket_uz_railway.domain.entity.carriage.TrainCarriageEntity;
import com.example.e_ticket_uz_railway.domain.entity.railwayFlight.RailwayFlightEntity;
import com.example.e_ticket_uz_railway.domain.entity.seat.SeatEntity;
import com.example.e_ticket_uz_railway.domain.entity.travel.TravelEntity;
import com.example.e_ticket_uz_railway.domain.enums.CarriageType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TrainCarriageService implements BaseService<CarriagePostRequest, BaseResponse<CarriageGetResponse>> {

    private final ModelMapper modelMapper;
    private final TrainCarriageDao carriageDao;
    private final RailwayFlightDao railwayFlightDao;
    private final TravelService travelService;
    private final TravelDao travelDao;

    @Override
    public BaseResponse<CarriageGetResponse> create(CarriagePostRequest carriagePostRequest) {
        TrainCarriageEntity carriageEntity = modelMapper.map(carriagePostRequest, TrainCarriageEntity.class);

        UUID railwayId = carriagePostRequest.getRailwayId();

        RailwayFlightEntity railwayFlightEntity = railwayFlightDao.findById(railwayId).get();
        carriageEntity.setRailways(railwayFlightEntity);

        Optional<List<TrainCarriageEntity>> optionalTrainCarriageEntityList = carriageDao
                .findTrainCarriageEntitiesByRailwaysId(railwayId);
        if (optionalTrainCarriageEntityList.isEmpty()) {
            carriageEntity.setCarriageNumber(1);
        } else {
            carriageEntity.setCarriageNumber(optionalTrainCarriageEntityList.get().size() + 1);
        }

        List<SeatEntity> seatEntities = createSeatEntitiesByCarriageType(carriageEntity);
        carriageEntity.setSeats(seatEntities);


        carriageDao.save(carriageEntity);

        return BaseResponse.<CarriageGetResponse>builder()
                .status(200)
                .message(carriageEntity.getCarriageType() + " has been added")
                .data(modelMapper.map(carriageEntity, CarriageGetResponse.class))
                .build();
    }


    private List<SeatEntity> createSeatEntitiesByCarriageType(TrainCarriageEntity carriageEntity) {
        CarriageType carriageType = carriageEntity.getCarriageType();
        int amountOfSeats;
        switch (carriageType) {
            case PLASKARD -> amountOfSeats = 15;
            case KUPE -> amountOfSeats = 10;
            default -> amountOfSeats = 5;
        }
        List<SeatEntity> seatEntities = new LinkedList<>();
        for (int i = 1; i <= amountOfSeats; i++) {
            seatEntities.add(new SeatEntity(i, carriageEntity));
        }
        return seatEntities;
    }

    public BaseResponse<List<CarriageSearchGetResponse>> searchCarriages(SearchingPostRequest searchingPostRequest,
                                                                         UUID railwayId) {

        List<TrainCarriageEntity> trainCarriageEntities = carriageDao
                .findTrainCarriageEntitiesByRailwaysId(railwayId).get();

        LinkedList<TravelEntity> travelEntities = travelDao
                .findTravelEntitiesByRailwaysIdOrderByCreated(railwayId).get();


        Map<String, Object> travelInfo = travelService
                .existsRailwayFlightTravelBySearching(searchingPostRequest, travelEntities);

        List<CarriageSearchGetResponse> carriageSearchGetResponses = new LinkedList<>();

        for (TrainCarriageEntity trainCarriage : trainCarriageEntities) {
            List<SeatEntity> availableSeats = travelService.findAvailableSeats(travelInfo, trainCarriage.getId());
            if (availableSeats.size()!=0){
                carriageSearchGetResponses.add(CarriageSearchGetResponse.builder()
                                .carriageType(trainCarriage.getCarriageType())
                                .travelPrice(getPriceByCarriageType(travelInfo, trainCarriage.getCarriageType()))
                                .seats(availableSeats)
                        .build());
            }
        }

        return BaseResponse.<List<CarriageSearchGetResponse>>builder()
                .status(200)
                .message(carriageSearchGetResponses.size() + " result(s) found")
                .data(carriageSearchGetResponses)
                .build();
    }

    private Double getPriceByCarriageType(Map<String, Object> travelInfo, CarriageType carriageType) {
        Object price;
        switch (carriageType){
            case PLASKARD -> price = travelInfo.get("plascardPrice");
            case KUPE -> price = travelInfo.get("kupePrice");
            default -> price = travelInfo.get("vipPrice");
        }
        return (Double) price;
    }

    @Override
    public BaseResponse<CarriageGetResponse> getById(UUID id) {
        return null;
    }

    @Override
    public BaseResponse<CarriageGetResponse> deleteById(UUID id) {
        return null;
    }
}
