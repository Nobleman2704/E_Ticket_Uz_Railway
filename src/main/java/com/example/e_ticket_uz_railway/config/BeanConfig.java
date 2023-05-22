package com.example.e_ticket_uz_railway.config;

import com.example.e_ticket_uz_railway.dao.UserDao;
import com.example.e_ticket_uz_railway.domain.dto.response.RailwayFlightGetResponse;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.util.List;


@Configuration
public class BeanConfig {
    private final UserDao userDao;

    public BeanConfig(UserDao userDao) {
        this.userDao = userDao;
    }

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return modelMapper;
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return email -> userDao
                .findUserEntityByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email is not found"));
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
