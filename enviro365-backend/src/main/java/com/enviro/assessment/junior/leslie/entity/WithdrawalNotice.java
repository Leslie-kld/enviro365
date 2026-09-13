package com.enviro.assessment.junior.leslie.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "withdrawal_notices")
public class WithdrawalNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "investor_id", nullable = false)
    private Investor investor;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal withdrawalAmount;

    @Column(nullable = false)
    private LocalDateTime withdrawalDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WithdrawalStatus status;

    public WithdrawalNotice() {
    }

    public WithdrawalNotice(Investor investor, Portfolio portfolio, BigDecimal withdrawalAmount,
                             LocalDateTime withdrawalDate, WithdrawalStatus status) {
        this.investor = investor;
        this.portfolio = portfolio;
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

    public Investor getInvestor() {
        return investor;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
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
