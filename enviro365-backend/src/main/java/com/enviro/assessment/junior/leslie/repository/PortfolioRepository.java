package com.enviro.assessment.junior.leslie.repository;

import com.enviro.assessment.junior.leslie.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    Optional<Portfolio> findByInvestorId(Long investorId);
}
