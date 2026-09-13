package com.enviro.assessment.junior.leslie.repository;

import com.enviro.assessment.junior.leslie.entity.InvestmentProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestmentProductRepository extends JpaRepository<InvestmentProduct, Long> {

    List<InvestmentProduct> findByPortfolioId(Long portfolioId);
}
