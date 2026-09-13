package com.enviro.assessment.junior.leslie.dto;

import java.math.BigDecimal;

public class InvestmentProductDTO {

    private Long id;
    private String productName;
    private String productType;
    private BigDecimal balance;

    public InvestmentProductDTO() {
    }

    public InvestmentProductDTO(Long id, String productName, String productType, BigDecimal balance) {
        this.id = id;
        this.productName = productName;
        this.productType = productType;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
