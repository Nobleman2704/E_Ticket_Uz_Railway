package com.example.e_ticket_uz_railway.service;

import com.example.e_ticket_uz_railway.dao.RailwayFlightDao;
import com.example.e_ticket_uz_railway.domain.dto.BaseResponse;
import com.example.e_ticket_uz_railway.domain.dto.request.RailwayFlightPostRequest;
import com.example.e_ticket_uz_railway.domain.dto.response.RailwayFlightGetResponse;
import com.example.e_ticket_uz_railway.domain.entity.railwayFlight.RailwayFlightEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RailwayFlightService implements BaseService<RailwayFlightPostRequest, BaseResponse<RailwayFlightGetResponse>>{
    private final RailwayFlightDao railwayFlightDao;
    private final ModelMapper modelMapper;

    @Override
    public BaseResponse<RailwayFlightGetResponse> create(RailwayFlightPostRequest railwayFlightPostRequest) {
        if (railwayFlightDao
                .existsRailwayFlightEntityByRailwayFlightName(railwayFlightPostRequest.getRailwayFlightName()))
            return BaseResponse.<RailwayFlightGetResponse>builder()
                    .status(400)
                    .message(railwayFlightPostRequest.getRailwayFlightName() + " already exists")
                    .build();

        RailwayFlightEntity railwayFlight = modelMapper.map(railwayFlightPostRequest, RailwayFlightEntity.class);

        railwayFlight.setExpirationDate(LocalDate.now().plusDays(railwayFlightPostRequest.getDuration()));

        railwayFlightDao.save(railwayFlight);

        return BaseResponse.<RailwayFlightGetResponse>builder()
                .status(200)
                .message("Success")
                .data(modelMapper.map(railwayFlight, RailwayFlightGetResponse.class))
                .build();
    }

//    public BaseResponse<List<RailwayFlightGetResponse>> findAll() {
//        List<RailwayFlightEntity> railwayFlightEntities = railwayFlightDao.findAll();
//        if (railwayFlightEntities.isEmpty()){
//            return BaseResponse.<List<RailwayFlightGetResponse>>builder()
//                    .status(404)
//                    .message("There is not any railway flights")
//                    .build();
//        }
//
//        return BaseResponse.<List<RailwayFlightGetResponse>>builder()
//                .status(404)
//                .message(railwayFlightEntities.size() + " result(s) found")
//                .data(modelMapper.map(railwayFlightEntities, new TypeToken<List<RailwayFlightGetResponse>>(){}
//                        .getType()))
//                .build();
//    }

    public List<RailwayFlightGetResponse> getAll(){
        return modelMapper.map(railwayFlightDao.findAll(), new TypeToken<List<RailwayFlightGetResponse>>(){}
                .getType());
    }


    @Override
    public BaseResponse<RailwayFlightGetResponse> getById(UUID id) {
        return null;
    }

    @Override
    public BaseResponse<RailwayFlightGetResponse> deleteById(UUID id) {
        return null;
    }
}
