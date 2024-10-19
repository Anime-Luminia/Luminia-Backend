package com.anime.luminia.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LuminiaUserRepositroy extends JpaRepository<LuminiaUser, Long> {
    Optional<LuminiaUser> findByEmail(String email);
}
