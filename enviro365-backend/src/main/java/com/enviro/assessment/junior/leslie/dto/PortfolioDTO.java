package com.enviro.assessment.junior.leslie.dto;

import java.math.BigDecimal;
import java.util.List;

public class PortfolioDTO {

    private Long id;
    private BigDecimal totalBalance;
    private BigDecimal maxWithdrawalAmount; // 90% of totalBalance, calculated for the UI
    private List<InvestmentProductDTO> investmentProducts;

    public PortfolioDTO() {
    }

    public PortfolioDTO(Long id, BigDecimal totalBalance, BigDecimal maxWithdrawalAmount,
                         List<InvestmentProductDTO> investmentProducts) {
        this.id = id;
        this.totalBalance = totalBalance;
        this.maxWithdrawalAmount = maxWithdrawalAmount;
        this.investmentProducts = investmentProducts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public BigDecimal getMaxWithdrawalAmount() {
        return maxWithdrawalAmount;
    }

    public void setMaxWithdrawalAmount(BigDecimal maxWithdrawalAmount) {
        this.maxWithdrawalAmount = maxWithdrawalAmount;
    }

    public List<InvestmentProductDTO> getInvestmentProducts() {
        return investmentProducts;
    }

    public void setInvestmentProducts(List<InvestmentProductDTO> investmentProducts) {
        this.investmentProducts = investmentProducts;
    }
}
