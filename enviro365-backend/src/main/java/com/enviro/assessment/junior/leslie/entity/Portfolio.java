package com.enviro.assessment.junior.leslie.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolios")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "investor_id", nullable = false, unique = true)
    private Investor investor;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalBalance;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvestmentProduct> investmentProducts = new ArrayList<>();

    public Portfolio() {
    }

    public Portfolio(Investor investor, BigDecimal totalBalance) {
        this.investor = investor;
        this.totalBalance = totalBalance;
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

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public List<InvestmentProduct> getInvestmentProducts() {
        return investmentProducts;
    }

    public void setInvestmentProducts(List<InvestmentProduct> investmentProducts) {
        this.investmentProducts = investmentProducts;
    }
}
