package com.library.userservice.Repository;

import com.library.userservice.Models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Override
    Token save (Token token);

    Optional<Token> findByValueAndDeleted(String token, boolean deleted);

    Optional<Token> findByValueAndDeletedAndExpiryAtGreaterThan(String tokenValue, boolean deleted, Date currentDate);
}
