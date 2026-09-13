package com.enviro.assessment.junior.leslie.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * Incoming payload for POST /api/withdrawals.
 * Field-level annotations cover structural validation (null / positive
 * checks). Business rules such as the age restriction and the 90% cap
 * are evaluated in the service layer, where the current portfolio
 * balance is available.
 */
public class WithdrawalRequestDTO {

    @NotNull(message = "investorId is required")
    private Long investorId;

    @NotNull(message = "portfolioId is required")
    private Long portfolioId;

    @NotNull(message = "withdrawalAmount is required")
    @Positive(message = "withdrawalAmount must be greater than zero")
    private BigDecimal withdrawalAmount;

    public WithdrawalRequestDTO() {
    }

    public Long getInvestorId() {
        return investorId;
    }

    public void setInvestorId(Long investorId) {
        this.investorId = investorId;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public BigDecimal getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public void setWithdrawalAmount(BigDecimal withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }
}
