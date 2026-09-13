package com.enviro.assessment.junior.leslie.service;

import com.enviro.assessment.junior.leslie.dto.*;
import com.enviro.assessment.junior.leslie.entity.Investor;
import com.enviro.assessment.junior.leslie.entity.InvestmentProduct;
import com.enviro.assessment.junior.leslie.entity.Portfolio;
import com.enviro.assessment.junior.leslie.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.leslie.repository.InvestorRepository;
import com.enviro.assessment.junior.leslie.repository.PortfolioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class InvestorService {

    // 90% cap used to show the investor the maximum they are allowed to withdraw
    private static final BigDecimal MAX_WITHDRAWAL_PERCENTAGE = new BigDecimal("0.90");

    private final InvestorRepository investorRepository;
    private final PortfolioRepository portfolioRepository;

    public InvestorService(InvestorRepository investorRepository, PortfolioRepository portfolioRepository) {
        this.investorRepository = investorRepository;
        this.portfolioRepository = portfolioRepository;
    }

    public List<InvestorDTO> getAllInvestors() {
        return investorRepository.findAll().stream()
                .map(this::toInvestorDTO)
                .toList();
    }

    public InvestorDTO getInvestorById(Long id) {
        Investor investor = findInvestorOrThrow(id);
        return toInvestorDTO(investor);
    }

    public InvestorPortfolioDTO getInvestorPortfolio(Long investorId) {
        Investor investor = findInvestorOrThrow(investorId);

        Portfolio portfolio = portfolioRepository.findByInvestorId(investorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No portfolio found for investor with id " + investorId));

        return new InvestorPortfolioDTO(toInvestorDTO(investor), toPortfolioDTO(portfolio));
    }

    public Investor findInvestorOrThrow(Long id) {
        return investorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found with id " + id));
    }

    private InvestorDTO toInvestorDTO(Investor investor) {
        return new InvestorDTO(
                investor.getId(),
                investor.getFirstName(),
                investor.getLastName(),
                investor.getAge(),
                investor.getEmail()
        );
    }

    private PortfolioDTO toPortfolioDTO(Portfolio portfolio) {
        List<InvestmentProductDTO> products = portfolio.getInvestmentProducts().stream()
                .map(this::toProductDTO)
                .toList();

        BigDecimal maxWithdrawal = portfolio.getTotalBalance()
                .multiply(MAX_WITHDRAWAL_PERCENTAGE)
                .setScale(2, RoundingMode.HALF_UP);

        return new PortfolioDTO(portfolio.getId(), portfolio.getTotalBalance(), maxWithdrawal, products);
    }

    private InvestmentProductDTO toProductDTO(InvestmentProduct product) {
        return new InvestmentProductDTO(
                product.getId(),
                product.getProductName(),
                product.getProductType(),
                product.getBalance()
        );
    }
}
