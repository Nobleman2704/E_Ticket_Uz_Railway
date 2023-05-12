package com.example.e_ticket_uz_railway.controller;

import com.example.e_ticket_uz_railway.domain.dto.BaseResponse;
import com.example.e_ticket_uz_railway.domain.dto.request.CardPostRequest;
import com.example.e_ticket_uz_railway.domain.dto.request.CardUpdateRequest;
import com.example.e_ticket_uz_railway.domain.dto.request.SearchingPostRequest;
import com.example.e_ticket_uz_railway.domain.dto.request.TicketPostRequest;
import com.example.e_ticket_uz_railway.domain.dto.response.CardGetResponse;
import com.example.e_ticket_uz_railway.domain.dto.response.CarriageSearchGetResponse;
import com.example.e_ticket_uz_railway.domain.dto.response.TicketGetResponse;
import com.example.e_ticket_uz_railway.domain.dto.response.TravelGetResponse;
import com.example.e_ticket_uz_railway.domain.entity.seat.SeatEntity;
import com.example.e_ticket_uz_railway.service.CardService;
import com.example.e_ticket_uz_railway.service.TicketService;
import com.example.e_ticket_uz_railway.service.TrainCarriageService;
import com.example.e_ticket_uz_railway.service.TravelService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final TravelService travelService;
    private final CardService cardService;
    private final TrainCarriageService trainCarriageService;
    private final TicketService ticketService;

    @GetMapping("/search")
    public ModelAndView searchTravels(
            @ModelAttribute("searchTravel") SearchingPostRequest searchingPostRequest) {
        BaseResponse<List<TravelGetResponse>> response = travelService
                .searchingTravels(searchingPostRequest);
        ModelAndView modelAndView = new ModelAndView("user-page");
        modelAndView.addObject("searching", searchingPostRequest);
        modelAndView.addObject("message", response.getMessage());
        modelAndView.addObject("travels", response.getData());
        return modelAndView;
    }


    @GetMapping("get_user_cards")
    public ModelAndView getUserCards(HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        BaseResponse<List<CardGetResponse>> response = cardService.getMyCardsById(userId);
        ModelAndView modelAndView = new ModelAndView("add-card");
        List<CardGetResponse> data = response.getData();
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("message", response.getMessage());
        modelAndView.addObject("cards", data);
        return modelAndView;
    }

    @PostMapping("/add_card")
    public ModelAndView addCard(@ModelAttribute("card") CardPostRequest cardPostRequest) {
        BaseResponse<CardGetResponse> response = cardService.create(cardPostRequest);
        ModelAndView modelAndView = new ModelAndView("add-card");
        UUID userId = cardPostRequest.getUserId();
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("message", response.getMessage());
        List<CardGetResponse> cards = cardService.getMyCardsById(userId).getData();
        modelAndView.addObject("cards", cards);
        return modelAndView;
    }

    @PostMapping("/add_balance")
    public ModelAndView addBalance(
            @ModelAttribute("cardUpdate") CardUpdateRequest cardUpdateRequest) {
        BaseResponse<CardGetResponse> response = cardService.fillBalance(cardUpdateRequest);
        ModelAndView modelAndView = new ModelAndView("add-card");
        UUID userId = response.getData().getUsers().getId();
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("message", response.getMessage());
        List<CardGetResponse> cards = cardService.getMyCardsById(userId).getData();
        modelAndView.addObject("cards", cards);
        return modelAndView;
    }

    @GetMapping("/get_carriages")
    public ModelAndView getCarriages(
            @ModelAttribute("searchTravel") SearchingPostRequest searchingPostRequest,
            @ModelAttribute("railwayId") UUID railwayId) {
        BaseResponse<List<CarriageSearchGetResponse>> response = trainCarriageService
                .searchCarriages(searchingPostRequest, railwayId);
        ModelAndView modelAndView = new ModelAndView("choose-carriage");
        modelAndView.addObject("message", response.getMessage());
        modelAndView.addObject("carriages", response.getData());
        return modelAndView;
    }

    @GetMapping("/get_seats")
    public ModelAndView getSeats(
            @ModelAttribute("searchTravel") SearchingPostRequest searchingPostRequest,
            @ModelAttribute("railwayId") UUID railwayId,
            @ModelAttribute("carriageNumber") int carriageNUmber) {
        ModelAndView modelAndView = new ModelAndView("choose-seat");

        modelAndView.addObject("search", searchingPostRequest);

        List<CarriageSearchGetResponse> carriages = trainCarriageService
                .searchCarriages(searchingPostRequest, railwayId).getData();
        for (CarriageSearchGetResponse carriage : carriages) {
            if (carriage.getCarriageNumber() == carriageNUmber) {
                modelAndView.addObject("travelPrice", carriage.getTravelPrice());
                modelAndView.addObject("seats", carriage.getSeats());
            }
        }
        return modelAndView;
    }

    @GetMapping("/choose_card")
    public ModelAndView chooseCard(HttpSession session,
                                   @ModelAttribute("searchTravel") SearchingPostRequest searchingPostRequest,
                                   @ModelAttribute("travelPrice") Double travelPrice,
                                   @ModelAttribute("seatId") UUID seatId) {
        UUID userId = (UUID) session.getAttribute("userId");

        ModelAndView modelAndView = new ModelAndView("choose-card");
        modelAndView.addObject("search", searchingPostRequest);
        modelAndView.addObject("travelPrice", travelPrice);
        modelAndView.addObject("seatId", seatId);
        BaseResponse<List<CardGetResponse>> response = cardService.getMyCardsById(userId);
        modelAndView.addObject("message", response.getMessage());
        modelAndView.addObject("cards", response.getData());
        return modelAndView;
    }

    @PostMapping("/book_seat")
    public ModelAndView bookSeat(@ModelAttribute("ticket")TicketPostRequest ticketPostRequest){
        BaseResponse<TicketGetResponse> response = ticketService.bookSeat(ticketPostRequest);
        ModelAndView modelAndView = new ModelAndView();
        String viewName;
        modelAndView.addObject("message", response.getMessage());
        if (response.getStatus()!=200){
            viewName = "choose-card";
            SearchingPostRequest searchingInfo = SearchingPostRequest.builder()
                    .cityFrom(ticketPostRequest.getCityFrom())
                    .cityTo(ticketPostRequest.getCityTo())
                    .localDate(ticketPostRequest.getLocalDate())
                    .build();
            modelAndView.addObject("search", searchingInfo);
            modelAndView.addObject("travelPrice", ticketPostRequest.getTravelPrice());
            modelAndView.addObject("seatId", ticketPostRequest.getSeatId());
        }else {
            viewName = "user-page";
        }
        modelAndView.setViewName(viewName);
        return modelAndView;
    }

    @GetMapping("/get_user_tickets")
    public ModelAndView getMyTickets(HttpSession session){
        UUID userId = (UUID) session.getAttribute("userId");
        BaseResponse<List<TicketGetResponse>> response = ticketService.findMyTicketsById(userId);
        ModelAndView modelAndView = new ModelAndView("my-tickets");
        modelAndView.addObject("message", response.getMessage());
        modelAndView.addObject("tickets", response.getData());
        return modelAndView;
    }
}