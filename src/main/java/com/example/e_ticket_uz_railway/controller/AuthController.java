package com.example.e_ticket_uz_railway.controller;

import com.example.e_ticket_uz_railway.domain.dto.BaseResponse;
import com.example.e_ticket_uz_railway.domain.dto.request.UserPostRequest;
import com.example.e_ticket_uz_railway.domain.dto.response.UserGetResponse;
import com.example.e_ticket_uz_railway.service.RailwayFlightService;
import com.example.e_ticket_uz_railway.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final RailwayFlightService railwayFlightService;

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute("user") UserPostRequest userPostRequest) {
        BaseResponse<UserGetResponse> response = userService.create(userPostRequest);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", response.getMessage());
        if (response.getStatus() == 200) {
            modelAndView.setViewName("sign-in");
        } else {
            modelAndView.setViewName("sign-up");
        }
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              HttpSession session){
        BaseResponse<UserGetResponse> response = userService.login(email, password);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", response.getMessage());

        if (response.getStatus()!=200){
            modelAndView.setViewName("sign-in");
        }else {
            session.setAttribute("userId", response.getData().getId());
            if (response.getData().isAdmin()) {
                modelAndView.addObject("railwayFlights", railwayFlightService.getAll());
                modelAndView.setViewName("admin-page");
            } else {
                modelAndView.setViewName("user-page");
            }
        }
        return modelAndView;
    }


    @GetMapping("/reg")
    public ModelAndView jumpRegister() {
        return new ModelAndView("sign-up");
    }


    @GetMapping("/log")
    public ModelAndView jumpLogin() {
        return new ModelAndView("sign-in");
    }
}
