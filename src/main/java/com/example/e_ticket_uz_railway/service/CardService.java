package com.example.e_ticket_uz_railway.service;

import com.example.e_ticket_uz_railway.dao.CardDao;
import com.example.e_ticket_uz_railway.dao.UserDao;
import com.example.e_ticket_uz_railway.domain.dto.BaseResponse;
import com.example.e_ticket_uz_railway.domain.dto.request.CardPostRequest;
import com.example.e_ticket_uz_railway.domain.dto.request.CardUpdateRequest;
import com.example.e_ticket_uz_railway.domain.dto.response.CardGetResponse;
import com.example.e_ticket_uz_railway.domain.entity.card.CardEntity;
import com.example.e_ticket_uz_railway.domain.entity.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService implements BaseService<CardPostRequest, BaseResponse<CardGetResponse>> {

    private final CardDao cardDao;
    private final ModelMapper modelMapper;
    private final UserDao userDao;

    @Override
    public BaseResponse<CardGetResponse> create(CardPostRequest cardPostRequest) {

        CardEntity cardEntity = modelMapper.map(cardPostRequest, CardEntity.class);
        UserEntity userEntity = userDao.findById(cardPostRequest.getUserId()).get();
        cardEntity.setUsers(userEntity);

        boolean check = isCardValid(cardEntity);

        if (check) {
            cardDao.save(cardEntity);
            return BaseResponse.<CardGetResponse>builder()
                    .status(200)
                    .message("Card added")
                    .data(modelMapper.map(cardEntity, CardGetResponse.class))
                    .build();
        }

        return BaseResponse.<CardGetResponse>builder()
                .status(400)
                .message("Card info is not valid")
                .build();
    }

    public BaseResponse<CardGetResponse> fillBalance(CardUpdateRequest cardUpdateRequest) {
        double balance = cardUpdateRequest.getBalance();
        if (isNegative(balance)) {
            return BaseResponse.<CardGetResponse>builder()
                    .status(400)
                    .message("Balance should be positive")
                    .build();
        }
        CardEntity cardEntity = cardDao.findById(cardUpdateRequest.getCardId()).get();
        cardEntity.setBalance(cardEntity.getBalance() + balance);

        cardDao.save(cardEntity);
        return BaseResponse.<CardGetResponse>builder()
                .status(200)
                .message("Card balance has been changed: " + cardEntity.getBalance())
                .data(modelMapper.map(cardEntity, CardGetResponse.class))
                .build();
    }

    public BaseResponse<List<CardGetResponse>> getMyCardsById(UUID userId) {
        Optional<List<CardEntity>> optionalCardEntities = cardDao.findCardEntitiesByUsersId(userId);
        if (optionalCardEntities.get().size()==0){
            return BaseResponse.<List<CardGetResponse>>builder()
                    .status(404)
                    .message("You do not have cards")
                    .build();
        }
        List<CardEntity> cardEntities = optionalCardEntities.get();

        return BaseResponse.<List<CardGetResponse>>builder()
                .status(200)
                .message(cardEntities.size() + " result(s) found")
                .data(modelMapper.map(cardEntities, new TypeToken<List<CardGetResponse>>(){}
                        .getType()))
                .build();
    }


    private boolean isCardValid(CardEntity cardEntity) {
        String cardNumber = cardEntity.getCardNumber();
        double balance = cardEntity.getBalance();
        if (!cardNumber.matches("\\d{16}")) {
            return false;
        } else if (isNegative(balance)) {
            return false;
        }
        List<CardEntity> userCards = cardEntity.getUsers().getCards();
        for (CardEntity userCard : userCards) {
            if (Objects.equals(userCard.getCardNumber(), cardNumber)) {
                return false;
            }
        }
        return true;
    }

    private boolean isNegative(double balance) {
        return balance <= 0;
    }


    @Override
    public BaseResponse<CardGetResponse> getById(UUID id) {
        return null;
    }

    @Override
    public BaseResponse<CardGetResponse> deleteById(UUID id) {
        return null;
    }
}
