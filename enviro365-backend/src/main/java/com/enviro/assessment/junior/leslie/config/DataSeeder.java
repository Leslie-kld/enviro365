package com.enviro.assessment.junior.leslie.config;

import com.enviro.assessment.junior.leslie.entity.Investor;
import com.enviro.assessment.junior.leslie.entity.InvestmentProduct;
import com.enviro.assessment.junior.leslie.entity.Portfolio;
import com.enviro.assessment.junior.leslie.repository.InvestmentProductRepository;
import com.enviro.assessment.junior.leslie.repository.InvestorRepository;
import com.enviro.assessment.junior.leslie.repository.PortfolioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Populates the in-memory H2 database with sample investors, portfolios
 * and investment products on startup, so the API and frontend have
 * realistic data to work with immediately.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final InvestorRepository investorRepository;
    private final PortfolioRepository portfolioRepository;
    private final InvestmentProductRepository investmentProductRepository;

    public DataSeeder(InvestorRepository investorRepository, PortfolioRepository portfolioRepository,
                       InvestmentProductRepository investmentProductRepository) {
        this.investorRepository = investorRepository;
        this.portfolioRepository = portfolioRepository;
        this.investmentProductRepository = investmentProductRepository;
    }

    @Override
    public void run(String... args) {
        Investor john = investorRepository.save(new Investor("John", "Smith", 70, "john.smith@example.com"));
        Investor sarah = investorRepository.save(new Investor("Sarah", "Johnson", 60, "sarah.johnson@example.com"));
        Investor michael = investorRepository.save(new Investor("Michael", "Brown", 75, "michael.brown@example.com"));

        seedPortfolio(john, new BigDecimal("500000.00"), new Object[][]{
                {"Retirement Annuity", "ANNUITY", new BigDecimal("300000.00")},
                {"Money Market Fund", "MONEY_MARKET", new BigDecimal("200000.00")}
        });

        seedPortfolio(sarah, new BigDecimal("250000.00"), new Object[][]{
                {"Preservation Fund", "PRESERVATION_FUND", new BigDecimal("150000.00")},
                {"Unit Trust", "UNIT_TRUST", new BigDecimal("100000.00")}
        });

        seedPortfolio(michael, new BigDecimal("820000.00"), new Object[][]{
                {"Retirement Annuity", "ANNUITY", new BigDecimal("500000.00")},
                {"Money Market Fund", "MONEY_MARKET", new BigDecimal("220000.00")},
                {"Unit Trust", "UNIT_TRUST", new BigDecimal("100000.00")}
        });
    }

    private void seedPortfolio(Investor investor, BigDecimal totalBalance, Object[][] products) {
        Portfolio portfolio = portfolioRepository.save(new Portfolio(investor, totalBalance));

        for (Object[] product : products) {
            investmentProductRepository.save(new InvestmentProduct(
                    (String) product[0],
                    (String) product[1],
                    (BigDecimal) product[2],
                    portfolio
            ));
        }
    }
}
