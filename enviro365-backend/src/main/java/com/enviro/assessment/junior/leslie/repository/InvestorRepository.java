package com.enviro.assessment.junior.leslie.repository;

import com.enviro.assessment.junior.leslie.entity.Investor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestorRepository extends JpaRepository<Investor, Long> {
}
