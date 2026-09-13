package com.enviro.assessment.junior.leslie.dto;

import com.enviro.assessment.junior.leslie.entity.WithdrawalStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WithdrawalResponseDTO {

    private Long id;
    private Long investorId;
    private String investorName;
    private Long portfolioId;
    private BigDecimal withdrawalAmount;
    private LocalDateTime withdrawalDate;
    private WithdrawalStatus status;

    public WithdrawalResponseDTO() {
    }

    public WithdrawalResponseDTO(Long id, Long investorId, String investorName, Long portfolioId,
                                  BigDecimal withdrawalAmount, LocalDateTime withdrawalDate,
                                  WithdrawalStatus status) {
        this.id = id;
        this.investorId = investorId;
        this.investorName = investorName;
        this.portfolioId = portfolioId;
        this.withdrawalAmount = withdrawalAmount;
        this.withdrawalDate = withdrawalDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInvestorId() {
        return investorId;
    }

    public void setInvestorId(Long investorId) {
        this.investorId = investorId;
    }

    public String getInvestorName() {
        return investorName;
    }

    public void setInvestorName(String investorName) {
        this.investorName = investorName;
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

    public LocalDateTime getWithdrawalDate() {
        return withdrawalDate;
    }

    public void setWithdrawalDate(LocalDateTime withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    public WithdrawalStatus getStatus() {
        return status;
    }

    public void setStatus(WithdrawalStatus status) {
        this.status = status;
    }
}
