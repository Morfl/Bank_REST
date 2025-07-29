package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<List<Card>> findAllByUserId(Long userId);

    Optional<List<Card>>findByUserId(Long userId);

    boolean existsByCardNumber(String cardNumber);

    Optional<List<Card>> findByUserEmail(String email);

    @Query("SELECT c.user FROM Card c WHERE c.id = :cardId")
    Optional<User> findUserByCardId(@Param("cardId") Long cardId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Card c " +
            "JOIN c.user u WHERE u.email = :email AND c.id = :cardId")
    boolean existsByUserEmailAndCardId(@Param("email") String email, @Param("cardId") Long cardId);
}
