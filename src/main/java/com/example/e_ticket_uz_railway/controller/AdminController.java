package com.example.e_ticket_uz_railway.controller;

import com.example.e_ticket_uz_railway.domain.dto.BaseResponse;
import com.example.e_ticket_uz_railway.domain.dto.request.CarriagePostRequest;
import com.example.e_ticket_uz_railway.domain.dto.request.RailwayFlightPostRequest;
import com.example.e_ticket_uz_railway.domain.dto.request.TravelPostRequest;
import com.example.e_ticket_uz_railway.domain.dto.response.CarriageGetResponse;
import com.example.e_ticket_uz_railway.domain.dto.response.RailwayFlightGetResponse;
import com.example.e_ticket_uz_railway.domain.dto.response.TravelGetResponse;
import com.example.e_ticket_uz_railway.service.RailwayFlightService;
import com.example.e_ticket_uz_railway.service.TrainCarriageService;
import com.example.e_ticket_uz_railway.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@EnableMethodSecurity
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @RequestMapping("/admin")
    public ModelAndView admin() {
        ModelAndView modelAndView = new ModelAndView("admin-page");
        modelAndView.addObject("railwayFlights", railwayFlightService.getAll());
        return modelAndView;
    }

    private final RailwayFlightService railwayFlightService;
    private final TravelService travelService;
    private final TrainCarriageService carriageService;

    @PostMapping("/add_railway_flight")
    public ModelAndView createRailwayFlight(
            @ModelAttribute("railwayFlight")RailwayFlightPostRequest railwayFlightPostRequest){

        BaseResponse<RailwayFlightGetResponse> response = railwayFlightService.create(railwayFlightPostRequest);

        ModelAndView modelAndView = new ModelAndView("admin-page");
        modelAndView.addObject("message", response.getMessage());
        modelAndView.addObject("railwayFlights", railwayFlightService.getAll());
        return modelAndView;
    }

    @GetMapping("/get_travels/{id}")
    public ModelAndView getTravels(@PathVariable("id")UUID railwayFlightId){
        LinkedList<TravelGetResponse> travels = (LinkedList<TravelGetResponse>)
                travelService.findTravelsByRailwayFlightId(railwayFlightId);

        ModelAndView modelAndView = new ModelAndView("add-travel");
        if (!travels.isEmpty()){
            modelAndView.addObject("cityFrom", travels.getLast().getCityTo());
            modelAndView.addObject("dateBegin", travels.getLast().getDateEnd());
        }

        modelAndView.addObject("railwayId", railwayFlightId);
        modelAndView.addObject("travels", travels);
        return modelAndView;
    }

    @PostMapping("/add_travel")
    public ModelAndView addTravel(@ModelAttribute("travel")TravelPostRequest travelPostRequest){
        BaseResponse<TravelGetResponse> response = travelService.create(travelPostRequest);
        ModelAndView modelAndView = new ModelAndView("add-travel");
        modelAndView.addObject("message", response.getMessage());
        UUID railwayId = travelPostRequest.getRailwayId();
        modelAndView.addObject("railwayId", railwayId);
        LinkedList<TravelGetResponse> travels = (LinkedList<TravelGetResponse>) travelService.findTravelsByRailwayFlightId(railwayId);

        if (!travels.isEmpty()){
            modelAndView.addObject("cityFrom", travels.getLast().getCityTo());
            modelAndView.addObject("dateBegin", travels.getLast().getDateEnd());
        }
        modelAndView.addObject("travels", travels);
        return modelAndView;
    }

    @GetMapping("/get_carriages/{id}")
    public ModelAndView getCarriages(@PathVariable("id") UUID railwayFlightId){
        List<CarriageGetResponse> carriages =  carriageService.findCarriagesByRailwayFlightId(railwayFlightId);
        ModelAndView modelAndView = new ModelAndView("add-carriage");
        modelAndView.addObject("carriages", carriages);
        modelAndView.addObject("railwayId", railwayFlightId);
        return modelAndView;
    }

    @PostMapping("/add_carriage")
    public ModelAndView addCarriage(@ModelAttribute("carriage")CarriagePostRequest carriagePostRequest){
        BaseResponse<CarriageGetResponse> response = carriageService.create(carriagePostRequest);
        ModelAndView modelAndView = new ModelAndView("add-carriage");
        modelAndView.addObject("message", response.getMessage());
        List<CarriageGetResponse> carriages = carriageService
                .findCarriagesByRailwayFlightId(carriagePostRequest.getRailwayId());
        modelAndView.addObject("railwayId", carriagePostRequest.getRailwayId());
        modelAndView.addObject("carriages", carriages);
        return modelAndView;
    }
}
