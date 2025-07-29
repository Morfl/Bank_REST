package com.example.bankcards.util;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.dto.UserDTO;

public class MapperUtils {

    public static CardDTO mapCardToCardDTO(Card card) {
        CardDTO cardDTO = new CardDTO();
        cardDTO.setId(card.getId());
        cardDTO.setCardNumber(CardNumberUtil.decryptPlusMaskNumber(card.getCardNumber()));
        cardDTO.setBalance(card.getBalance());
        cardDTO.setStatus(card.getStatus().getDescription());
        cardDTO.setOwnerEmail(card.getUser().getEmail());
        cardDTO.setExpiryDate(card.getExpiryDate());
        return cardDTO;
    }

    public static UserDTO mapUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullName(user.getFullName());
        userDTO.setRole(user.getRole());
        return userDTO;
    }
}

