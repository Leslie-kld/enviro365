package com.enviro.assessment.junior.leslie.dto;

/**
 * Combined response returned by GET /api/investors/{id}/portfolio.
 * Wraps investor details together with their portfolio so the frontend
 * dashboard can be built from a single API call.
 */
public class InvestorPortfolioDTO {

    private InvestorDTO investor;
    private PortfolioDTO portfolio;

    public InvestorPortfolioDTO() {
    }

    public InvestorPortfolioDTO(InvestorDTO investor, PortfolioDTO portfolio) {
        this.investor = investor;
        this.portfolio = portfolio;
    }

    public InvestorDTO getInvestor() {
        return investor;
    }

    public void setInvestor(InvestorDTO investor) {
        this.investor = investor;
    }

    public PortfolioDTO getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(PortfolioDTO portfolio) {
        this.portfolio = portfolio;
    }
}
